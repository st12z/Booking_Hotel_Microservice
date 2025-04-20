package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.RoomTypeConverter;
import com.thuc.rooms.dto.CheckRoomDto;
import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.dto.SearchDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.RoomType;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.repository.RoomTypeRepository;
import com.thuc.rooms.service.IRedisHoldRooms;
import com.thuc.rooms.service.IRoomTypeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    @PersistenceContext
    private EntityManager entityManager;
    // Trả về room type mà còn phòng đến tại thời điểm bây giờ
    // Lấy được Property => Query các RoomType có propertyId => Có phòng thuộc loại RoomType mà checkOun< currentDateTime
    // tìm kiếm phòng có sẵn tại thời điểm hiện tại
    @Override
    public List<RoomTypeDto> getAllRoomTypes(String slugProperty) {
        logger.debug("Requested to getAllRoomTypes successfully");
        Property property = propertyRepository.findBySlug(slugProperty);
        if(property == null) {
            throw new ResourceNotFoundException("Property","Slug",slugProperty);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        StringBuilder sql =new StringBuilder("SELECT * FROM room_type rt WHERE rt.property_id=:propertyId ") ;
        sql.append(" AND EXISTS (SELECT 1 FROM rooms r WHERE rt.id = r.room_type_id AND (r.check_out <:currentDateTime OR r.check_out is null)) ");
        Query query = entityManager.createNativeQuery(sql.toString(), RoomType.class);
        query.setParameter("currentDateTime", currentDateTime);
        query.setParameter("propertyId", property.getId());
        List<RoomType> roomTypes = query.getResultList();
        return roomTypes.stream().map(RoomTypeConverter::toRoomTypDto).toList();

    }
    // Tìm kiếm phòng có sẵn của 1 property theo điều kiện checkin, checkout
    @Override
    public List<RoomTypeDto> getAllRoomTypesBySearch(String slugProperty,SearchDto searchDto) {
        logger.debug("Requested to getAllRoomTypesBySearch successfully");
        Property property = propertyRepository.findBySlug(slugProperty);
        if(property == null) {
            throw new ResourceNotFoundException("Property","Slug",slugProperty);
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM room_type rt WHERE rt.property_id=:propertyId ") ;
        if(searchDto.getQuantityBeds()!=null){
            sql.append(" AND rt.num_beds=:quantityBeds");
        }
        if(searchDto.getCheckIn()!=null ){
            sql.append(" AND ( EXISTS (SELECT 1 FROM rooms r WHERE r.room_type_id=rt.id AND (r.check_out <:checkIn OR (r.check_in is null AND r.check_out is null))) ");
        }
        else{
            sql.append(" AND ( EXISTS (SELECT 1 FROM rooms r WHERE r.room_type_id=rt.id AND (r.check_out <:currentDateTime OR (r.check_in is null AND r.check_out is null)))");
        }
        if(searchDto.getCheckOut()!=null){
            sql.append(" OR EXISTS (SELECT 1 FROM rooms r WHERE r.room_type_id=rt.id AND (r.check_in >:checkOut OR (r.check_in is null AND r.check_out is null))))");
        }
        else{
            sql.append(" OR EXISTS (SELECT 1 FROM rooms r WHERE r.room_type_id=rt.id AND (r.check_in >:currentDateTime OR (r.check_in is null AND r.check_out is null))))");
        }
        Query query = entityManager.createNativeQuery(sql.toString(), RoomType.class);
        if(searchDto.getCheckIn()!=null ){
            query.setParameter("checkIn", searchDto.getCheckIn());
        }
        else{
            query.setParameter("currentDateTime", LocalDateTime.now());
        }
        if(searchDto.getCheckOut()!=null ){
            query.setParameter("checkOut", searchDto.getCheckOut());
        }
        else{
            query.setParameter("currentDateTime", LocalDateTime.now());
        }
        if(searchDto.getQuantityBeds()!=null){
            query.setParameter("quantityBeds", searchDto.getQuantityBeds());
        }

        query.setParameter("propertyId", property.getId());
        List<RoomType> roomTypes = query.getResultList();
        return roomTypes.stream().map(RoomTypeConverter::toRoomTypDto).toList();

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
            logger.debug("countRemainRooms: {}",countRemainRooms);
            logger.debug("countRoomsInRedis: {}",countRoomsInRedis);
            int actutalAvailable = countRemainRooms - countRoomsInRedis;
            logger.debug("actutalAvailable: {}",actutalAvailable);
            logger.debug("quantity from request:{}",checkRoomDto.getQuantity());
            if(actutalAvailable< checkRoomDto.getQuantity()) {
                redisHoldRooms.deleteData(key);
            }
            return actutalAvailable;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    private String getLockKeyFromCheckRoomDto(CheckRoomDto checkRoomDto) {
        return String.format("lock|room|%d",checkRoomDto.getRoomTypeId());
    }

    @Override
    public RoomTypeDto getRoomTypeById(Integer id) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("RoomType","id",String.valueOf(id)));
        return RoomTypeConverter.toRoomTypDto(roomType);
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
        StringBuilder builder = new StringBuilder(" SELECT COUNT(r.id) FROM room_type rt " +
                "LEFT JOIN rooms r on rt.id=r.room_type_id and (r.check_out <:checkIn or r.check_out is null or r.check_in >:checkOut) WHERE 1=1 ");
        builder.append(" AND rt.id=:roomTypeId ");
        Query query = entityManager.createNativeQuery(builder.toString(), Integer.class);
        query.setParameter("checkIn", checkRoomDto.getCheckIn());
        query.setParameter("checkOut", checkRoomDto.getCheckOut());
        query.setParameter("roomTypeId",checkRoomDto.getRoomTypeId());
        return ((Number) query.getSingleResult()).intValue();

    }

}
