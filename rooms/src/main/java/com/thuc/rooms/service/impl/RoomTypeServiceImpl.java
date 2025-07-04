package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomTypeConverter;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.*;
import com.thuc.rooms.exception.ResourceAlreadyExistsException;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.*;
import com.thuc.rooms.service.IRedisHoldRooms;
import com.thuc.rooms.service.IRoomTypeService;
import com.thuc.rooms.service.client.BookingsFeignClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// class check phòng của 1 property
@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements IRoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final PropertyRepository propertyRepository;
    private final Logger logger = LoggerFactory.getLogger(RoomTypeServiceImpl.class);
    private final RedisTemplate<String,Object> redisTemplate;
    private final IRedisHoldRooms redisHoldRooms;
    private final RedissonClient redissonClient;
    private final RoomRepository roomRepository;
    private final UserDiscountRepository userDiscountRepository;
    private final UserDiscountCarsRepository userDiscountCarsRepository;
    private final BookingsFeignClient bookingsFeignClient;
    private final FacilitiesRepository facilitiesRepository;
    @PersistenceContext
    private EntityManager entityManager;
    // Trả về room type mà còn phòng đến tại thời điểm bây giờ
    // Lấy được Property => Query các RoomType có propertyId => Có phòng thuộc loại RoomType mà checkOun< currentDateTime
    // tìm kiếm phòng có sẵn tại thời điểm hiện tại
    @Override
    public List<RoomTypeDto> getAllRoomTypesBySlug(String slugProperty) {
        logger.debug("Requested to getAllRoomTypes successfully");
        Property property = propertyRepository.findBySlug(slugProperty);

        if(property == null) {
            throw new ResourceNotFoundException("Property","Slug",slugProperty);
        }
        int propertyId= property.getId();
        List<BookingRoomsDto> bookingRoomsDtos = getBookingRooms(null,propertyId);
        logger.debug("bookingRoomDtos :{}",bookingRoomsDtos);

        List<RoomType> roomTypes = roomTypeRepository.findByPropertyId(propertyId);
        logger.debug("roomTypes :{}",roomTypes);
        List<RoomTypeDto> result = roomTypes.stream().filter(rt -> {
            return checkBookingRooms(rt.getId(),propertyId,bookingRoomsDtos,null,null);

        }).map(RoomTypeConverter::toRoomTypDto).toList();
        return result;
    }


    // Tìm kiếm phòng có sẵn của 1 property theo điều kiện checkin, checkout
    @Override
    public List<RoomTypeDto> getAllRoomTypesBySearch(String slugProperty,SearchDto searchDto) {
        logger.debug("Requested to getAllRoomTypesBySearch successfully");
        Property property = propertyRepository.findBySlug(slugProperty);
        if(property == null) {
            throw new ResourceNotFoundException("Property","Slug",slugProperty);
        }
        int propertyId = property.getId();
        StringBuilder sql = new StringBuilder("SELECT * FROM room_type rt WHERE rt.property_id=:propertyId ") ;
        if(searchDto.getQuantityBeds()!=null){
            sql.append(" AND rt.num_beds=:quantityBeds");
        }
        Query query = entityManager.createNativeQuery(sql.toString(), RoomType.class);
        if(searchDto.getQuantityBeds()!=null){
            query.setParameter("quantityBeds", searchDto.getQuantityBeds());
        }
        query.setParameter("propertyId", propertyId);
        List<RoomType> roomTypes = query.getResultList();
        List<BookingRoomsDto> bookingRoomsDtos = getBookingRooms(null,propertyId);
        List<RoomTypeDto> result = roomTypes.stream().filter(rt -> {
            return checkBookingRooms(rt.getId(),propertyId,bookingRoomsDtos,searchDto.getCheckIn(),searchDto.getCheckOut());
        }).map(RoomTypeConverter::toRoomTypDto).toList();
        return result;

    }
    // kiem tra xem du phong khong
    // redis: ckp:36 - checkRoomDto, thuc:36 - checkRoomDto

    // A vào check luu vao redis hold-rooms:36: + ckp:4/2:7/2
    //                                          + ac:8/2:9/2
    //                                          + thuc: 5/2 : 8/2
    // B vào check trong roomTypeId do trong redis. neu ton tai lay so luong ra. Tong so
    // luong phong thuc te dang trong db - so luong trong redis , neu con thi luu vao redis
    // problem : + total :3
    //           : + A giu ca 3 trong thoi gian 6-2 ->7/2
    //           : + B giu 2 trong thoi gian 8-2 ->9/2
    @Override
    public Integer checkEnoughRooms(CheckRoomDto checkRoomDto) {

        try{
            String key = getKeyFromCheckRoomDto(checkRoomDto);
            int countRemainRooms = getQuantityRoomsRemain(checkRoomDto); // so luong phong con lai trong db trong thoi gian cua checkRoomDto
            int countRoomsInRedis = getTotalRoomsInRedis(checkRoomDto);
            logger.debug("-----------------");
            logger.debug("Thread name: {}",Thread.currentThread().getName());
            logger.debug("email: {}",checkRoomDto.getEmail());
            logger.debug("countRemainRooms: {}",countRemainRooms);
            logger.debug("countRoomsInRedis: {}",countRoomsInRedis);
            int actutalAvailable = countRemainRooms - countRoomsInRedis;
            logger.debug("actutalAvailable: {}",actutalAvailable);
            logger.debug("quantity from request:{}",checkRoomDto.getQuantity());
            logger.debug("-----------------");
            if(actutalAvailable< checkRoomDto.getQuantity()) {
                redisHoldRooms.deleteData(key);
            }
            return actutalAvailable;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public boolean holdRooms(List<CheckRoomDto> roomReversed) {
        boolean check=checkEnoughRoomsAgain(roomReversed);
        logger.debug("check :{}",check);
        if(check){
            for(CheckRoomDto checkRoomDto : roomReversed){
                redisHoldRooms.saveData(getKeyFromCheckRoomDto(checkRoomDto),checkRoomDto);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean checkHoldRooms(BookingDto bookingDto) {
        try{
           boolean check = true;
           for(BookingRoomTypeDto roomTypeDto : bookingDto.getRoomTypes()){
               CheckRoomDto checkRoomDto = CheckRoomDto.builder()
                       .checkIn(roomTypeDto.getCheckIn())
                       .checkOut(roomTypeDto.getCheckOut())
                       .roomTypeId(roomTypeDto.getRoomTypeId())
                       .email(bookingDto.getUserEmail())
                       .quantity(roomTypeDto.getQuantityRooms())
                       .build();
               String key = getKeyFromCheckRoomDto(checkRoomDto);
               logger.debug("169-key exists: {}",key);
               if(!redisTemplate.hasKey(key)){
                   return false;
               }
           }
           return check;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
    // Tìm hai phòng trống
    @Override
    public List<Integer> getAvailableRooms(BookingRoomTypeDto bookingRoomTypeDto, int propertyId) {
        int roomTypeId= bookingRoomTypeDto.getRoomTypeId();
        int quantityRooms = bookingRoomTypeDto.getQuantityRooms();
        StringBuilder builder = new StringBuilder("SELECT * FROM rooms WHERE 1=1 ");
        builder.append(" AND rooms.property_id =:propertyId ");
        builder.append(" AND rooms.room_type_id =:roomTypeId ");
        Query query = entityManager.createNativeQuery(builder.toString(),Room.class);
        query.setParameter("propertyId",propertyId);
        query.setParameter("roomTypeId",roomTypeId);
        List<Room> rooms = query.getResultList();
        List<BookingRoomsDto> bookingRoomsDtos = getBookingRooms(roomTypeId,propertyId);
        BookingRoomsDto bookedListRooms = null; // loai phong da dat co propertyId,roomTypeId bang roomTypeId,propertyId
        List<Integer> result = new ArrayList<>();
        Set<Integer> bookedNow = new HashSet<>();
        if(!bookingRoomsDtos.isEmpty()){
            for(BookingRoomsDto bookingRoomsDto : bookingRoomsDtos){
                if(bookingRoomsDto.getRoomTypeId()==roomTypeId && bookingRoomsDto.getPropertyId()==propertyId){
                    LocalDateTime checkIn = bookingRoomsDto.getCheckIn();
                    LocalDateTime checkOut = bookingRoomsDto.getCheckOut();
                    if(!(bookingRoomTypeDto.getCheckIn().isAfter(checkOut) || bookingRoomTypeDto.getCheckOut().isBefore(checkIn))){
                        bookedNow.addAll(bookingRoomsDto.getNumRooms());
                    }
                }
            }
        }
        for(Room room: rooms){
            if(!bookedNow.contains(room.getRoomNumber())){
                result.add(room.getRoomNumber());
                if(result.size()==quantityRooms) break;
            }
        }
        return result;
    }

    @Override
    public boolean confirmBooking(BookingRoomConfirmDto bookingRoomConfirmDto,Integer discountCarId,Integer discountHotelId) {
        logger.debug("discountCarId:{}",discountCarId);
        logger.debug("discountHotelId:{}",discountHotelId);
        for(int roomNumber : bookingRoomConfirmDto.getNumRooms()){
            Room room = roomRepository.findByRoomNumberAndPropertyIdAndRoomTypeId(roomNumber
                    ,bookingRoomConfirmDto.getPropertyId()
                    ,bookingRoomConfirmDto.getRoomTypeId());
            logger.debug("room :{}",room);
            if(room!=null){
                room.setCheckIn(bookingRoomConfirmDto.getCheckIn());
                room.setCheckOut(bookingRoomConfirmDto.getCheckOut());
                roomRepository.save(room);
            }
        }
        if(discountHotelId!=null && discountHotelId!=-1){
            UserDiscount userDiscount = userDiscountRepository.findByDiscountIdAndEmail(discountHotelId,bookingRoomConfirmDto.getUserEmail());
            if(userDiscount!=null){
                userDiscountRepository.delete(userDiscount);
            }
        }
        if(discountCarId!=null && discountCarId!=-1){
            UserDiscountCars userDiscountCars = userDiscountCarsRepository.findByDiscountCarIdAndEmail(discountCarId,bookingRoomConfirmDto.getUserEmail());
            if(userDiscountCars!=null){
                userDiscountCarsRepository.delete(userDiscountCars);
            }
        }

        return true;
    }

    @Override
    public List<RoomTypeDto> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return roomTypes.stream().map(RoomTypeConverter::toRoomTypDto).toList();
    }

    @Override
    public RoomTypeDto createRoomType(RoomTypeRequestDto roomTypeDto) {
        RoomType existRoomType = roomTypeRepository.findByName(roomTypeDto.getName());
        if(existRoomType!=null){
            throw new ResourceAlreadyExistsException("RoomType","name",roomTypeDto.getName());
        }
        Property property = propertyRepository.findById(roomTypeDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property","id",String.valueOf(roomTypeDto.getPropertyId())));
        List<Facilities> facilities = roomTypeDto.getFreeServices().stream().map(id->{
            Facilities facility = facilitiesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Facilities","id",String.valueOf(id)));
            return facility;
        }).collect(Collectors.toList());

        RoomType roomType = RoomType.builder()
                .name(roomTypeDto.getName())
                .area(roomTypeDto.getArea())
                .price(roomTypeDto.getPrice())
                .freeServices(facilities)
                .discount(roomTypeDto.getDiscount())
                .numBeds(roomTypeDto.getNumBeds())
                .property(property)
                .status(true)
                .freeServices(facilities)
                .maxGuests(roomTypeDto.getMaxGuests())
                .build();
        RoomType savedRoomType=roomTypeRepository.save(roomType);
        for(Facilities facility : facilities){
            facility.getRoomTypes().add(roomType);
            facilitiesRepository.save(facility);
        }

        roomTypeDto.getRooms().forEach(roomNumber->{
            Room room = Room.builder()
                    .roomNumber(roomNumber)
                    .property(property)
                    .roomType(savedRoomType)
                    .status("available")
                    .build();
            roomRepository.save(room);
        });

        return RoomTypeConverter.toRoomTypDto(savedRoomType);

    }

    @Override
    public RoomTypeDto updateRoomType(Integer id, RoomTypeRequestDto roomTypeDto) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RoomType","id",String.valueOf(id)));
        roomRepository.deleteByRoomTypeIdAndPropertyId(id,roomTypeDto.getPropertyId());
        roomTypeDto.getRooms().forEach(roomNumber->{
            Room room = Room.builder()
                    .roomNumber(roomNumber)
                    .roomType(roomType)
                    .property(roomType.getProperty())
                    .status("available")
                    .build();
            roomRepository.save(room);
        });
        roomType.getFreeServices().clear();
        List<Facilities> oldFacilities = roomType.getFreeServices();
        List<Facilities> facilities =roomTypeDto.getFreeServices().stream().map(facilityId->{
          Facilities findFacility = facilitiesRepository.findById(facilityId).orElseThrow(() -> new ResourceNotFoundException("Facilities","id",String.valueOf(facilityId)));
          return findFacility;
        }).toList();
        roomType.setArea(roomType.getArea());
        roomType.setPrice(roomType.getPrice());
        roomType.setNumBeds(roomType.getNumBeds());
        roomType.setMaxGuests(roomType.getMaxGuests());
        roomType.setName(roomType.getName());
        roomType.setDiscount(roomType.getDiscount());
        RoomType newRoomType=roomTypeRepository.save(roomType);
        for(Facilities facility : oldFacilities){
            facility.getRoomTypes().remove(roomType);
            facilitiesRepository.save(facility);
        }
        for(Facilities facility : facilities){
            facility.getRoomTypes().add(newRoomType);
            facilitiesRepository.save(facility);
        }
        return RoomTypeConverter.toRoomTypDto(newRoomType);
    }


    private boolean checkEnoughRoomsAgain(List<CheckRoomDto> roomReversed) {
        for(CheckRoomDto checkRoomDto : roomReversed){
            boolean getLock = false;
            RLock lock = redissonClient.getLock(getLockKeyFromCheckRoomDto(checkRoomDto));
            try{
                getLock = lock.tryLock(10,5,TimeUnit.SECONDS);
                if(!getLock){
                    throw new RuntimeException("Lock is locked! Please try again");
                }
                int actutalAvailable = checkEnoughRooms(checkRoomDto);
                if(actutalAvailable < checkRoomDto.getQuantity()) {
                    return false;
                }
            }catch (InterruptedException e){
                throw new RuntimeException("Failure get lock");
            }finally {
                if(lock.isLocked() && lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        }
        return true;
    }

    private String getLockKeyFromCheckRoomDto(CheckRoomDto checkRoomDto) {
        return String.format("lock|room|%d",checkRoomDto.getRoomTypeId());
    }



    @Override
    public RoomTypeDto getRoomTypeById(Integer id) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("RoomType","id",String.valueOf(id)));
        return RoomTypeConverter.toRoomTypDto(roomType);
    }

    private boolean checkBookingRooms(Integer roomTypeId, int propertyId,
                                      List<BookingRoomsDto> bookingRoomsDtos,
                                      LocalDateTime checkIn,
                                      LocalDateTime checkOut
    ) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Integer> roomNumbers = roomRepository.findByRoomTypeIdAndPropertyId(roomTypeId, propertyId)
                .stream().map(Room::getRoomNumber).toList();
        logger.debug("roomNumbers :{}",roomNumbers);
        Set<Integer> bookedNow = new HashSet<>();
        if(!bookingRoomsDtos.isEmpty()){
            for (BookingRoomsDto bookingRoomsDto : bookingRoomsDtos) {
                if (bookingRoomsDto.getRoomTypeId() == roomTypeId && bookingRoomsDto.getPropertyId() == propertyId) {
                    LocalDateTime checkInRoom = bookingRoomsDto.getCheckIn();
                    LocalDateTime checkOutRoom = bookingRoomsDto.getCheckOut();
                    if(checkIn==null && checkOut==null){
                        if(!(currentDateTime.isBefore(checkInRoom) || currentDateTime.isAfter(checkOutRoom))){
                            bookedNow.addAll(bookingRoomsDto.getNumRooms());
                        }
                    }
                    else{
                        if(!(checkOut.isBefore(checkInRoom) || Objects.requireNonNull(checkIn).isAfter(checkOutRoom))){
                            bookedNow.addAll(bookingRoomsDto.getNumRooms());
                        }
                    }
                }
            }
        }
        logger.debug("bookedNow :{}",bookedNow);
        logger.debug("----------------------");
        return roomNumbers.stream().anyMatch(number -> !bookedNow.contains(number));
    }

    public  List<BookingRoomsDto> getBookingRooms(Integer roomTypeId,Integer propertyId){
        SuccessResponseDto<List<BookingRoomsDto> > response = bookingsFeignClient.getBookingRooms(roomTypeId,propertyId).getBody();
        return response.getData();
    }

    private String getKeyFromCheckRoomDto(CheckRoomDto checkRoomDto) {
        return String.format("hold|room|%d|%s|%s|%s",
                checkRoomDto.getRoomTypeId(),
                checkRoomDto.getEmail(),
                checkRoomDto.getCheckIn(),
                checkRoomDto.getCheckOut());
    }



    public int getTotalRoomsInRedis(CheckRoomDto checkRoomDto) {
        try {
            String pattern = String.format("hold|room|%d|*", checkRoomDto.getRoomTypeId());
            Set<String> keys = redisTemplate.keys(pattern);
            logger.debug("keys: {}", keys);
            if (keys.isEmpty()) return 0;
            int total = 0;
            LocalDateTime checkIn = checkRoomDto.getCheckIn();
            LocalDateTime checkOut = checkRoomDto.getCheckOut();
            String currentKey = getKeyFromCheckRoomDto(checkRoomDto); // Để tránh tự tính bản thân
            logger.debug("currentKey: {}", currentKey);
            for (String key : keys) {
                if (key.equals(currentKey)) continue;
                logger.debug("key: {}", key);
                String[] parts = key.split("\\|"); // Trích xuất phần email&checkIn&checkOut
                LocalDateTime holdCheckIn = LocalDateTime.parse(parts[4]);
                LocalDateTime holdCheckOut = LocalDateTime.parse(parts[5]);

                if (!(checkIn.isAfter(holdCheckOut) || checkOut.isBefore(holdCheckIn))) {
                    Integer heldRooms = redisHoldRooms.getData(key); // key đầy đủ
                    logger.debug("heldRooms: {}", heldRooms);
                    total += heldRooms != null ? heldRooms : 0;
                }
            }

            return total;

        } catch (Exception e) {
            throw new RuntimeException("getTotalRoomsInRedis error", e);
        }
    }


    private int getQuantityRoomsRemain(CheckRoomDto checkRoomDto) {
        Integer propertyId = checkRoomDto.getPropertyId();
        Integer roomTypeId = checkRoomDto.getRoomTypeId();
        List<BookingRoomsDto> bookingRoomsDtos = getBookingRooms(checkRoomDto.getRoomTypeId(),checkRoomDto.getPropertyId());
        logger.debug("bookingRoomDtos :{}",bookingRoomsDtos);
        List<Room> rooms = roomRepository.findByRoomTypeIdAndPropertyId(checkRoomDto.getRoomTypeId(), checkRoomDto.getPropertyId());
        Set<Integer> bookedNow = new HashSet<>();
        if(!bookingRoomsDtos.isEmpty()){
            for(BookingRoomsDto bookingRoomsDto : bookingRoomsDtos){
                if(bookingRoomsDto.getRoomTypeId()==roomTypeId && bookingRoomsDto.getPropertyId()==propertyId){
                    LocalDateTime checkIn = bookingRoomsDto.getCheckIn();
                    LocalDateTime checkOut = bookingRoomsDto.getCheckOut();
                    if(!(checkRoomDto.getCheckIn().isAfter(checkOut) || checkRoomDto.getCheckOut().isBefore(checkIn))){
                        bookedNow.addAll(bookingRoomsDto.getNumRooms());
                    }
                }
            }
        }
        if(bookedNow.isEmpty()){
            return rooms.size();
        }
        int count =0;
        for(Room room : rooms){
            if(!bookedNow.contains(room.getRoomNumber())) count+=1;
        }
        return count;
    }

}
