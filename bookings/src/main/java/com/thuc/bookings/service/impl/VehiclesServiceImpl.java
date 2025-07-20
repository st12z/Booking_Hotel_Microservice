package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.VehicleConverter;
import com.thuc.bookings.dto.requestDto.*;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.exception.ResourceAlreadyExistsException;
import com.thuc.bookings.exception.ResourceNotFoundException;
import com.thuc.bookings.repository.VehiclesRepository;
import com.thuc.bookings.service.IRedisVehicleService;
import com.thuc.bookings.service.IVehiclesService;
import com.thuc.bookings.utils.CarStatus;
import com.thuc.bookings.utils.CarType;
import com.thuc.bookings.utils.UploadCloudinary;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VehiclesServiceImpl implements IVehiclesService {
    private final VehiclesRepository vehiclesRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final IRedisVehicleService redisVehicleService;
    private final Logger logger = LoggerFactory.getLogger(VehiclesServiceImpl.class);
    private final UploadCloudinary uploadCloudinary;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VehicleDto> getAllVehicles(FilterCarDto filterDto) {
        StringBuilder builder = new StringBuilder("SELECT * FROM vehicles WHERE 1=1 AND status=:status");
        // noi dieu kien
        if(filterDto.getChoosedPrice()!=0){
            builder.append(" AND price <=:price ");
        }
        if(!filterDto.getChoosedStars().isEmpty() && !filterDto.getChoosedStars().contains(0)){
            builder.append(" AND star IN (:stars) ");
        }
        if(!filterDto.getChoosedCarTypes().isEmpty()){
            builder.append(" AND car_type IN (:car_types) ");
        }

        Query query = entityManager.createNativeQuery(builder.toString(),Vehicles.class);
        // set parameter
        if(filterDto.getChoosedPrice()!=0){
            query.setParameter("price",filterDto.getChoosedPrice());
        }
        if(!filterDto.getChoosedStars().isEmpty()){
            query.setParameter("stars",filterDto.getChoosedStars());
        }
        if(!filterDto.getChoosedCarTypes().isEmpty()){
            query.setParameter("car_types",filterDto.getChoosedCarTypes());
        }
        query.setParameter("status",CarStatus.AVAILABLE.getValue());
        logger.debug("query: {}", query);
        List<Vehicles> vehicles =(List<Vehicles>) query.getResultList();
        return vehicles.stream().map(VehicleConverter::toVehicleDto).toList();
    }

    @Override
    public boolean holdVehicle(VehicleHoldDto vehicleDto) {
        Vehicles vehicles = vehiclesRepository.findById(vehicleDto.getVehicleId())
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle","id",String.valueOf(vehicleDto.getVehicleId())));
        if(vehicles.getQuantity() == 0 || !vehicles.getStatus().getValue().equals("AVAILABLE")){
            return false;
        }
        boolean locked = false;
        RLock lock = redissonClient.getLock(String.format("%d",vehicleDto.getVehicleId()));
        try{
            locked = lock.tryLock(10,5, TimeUnit.SECONDS);
            if(!locked){
                throw new RuntimeException("Lock is locked.Please try again!");
            }
            logger.debug("Thread name is {}", Thread.currentThread().getName());
            logger.debug("Holding vehicle {}", vehicleDto);
            String key = getKeyFromVehicleDto(vehicleDto);
            logger.debug("key {}", key);
            int quantityInDB = totalQuantityInDB(vehicleDto);
            int quantityInRedis  = totalQuantityInRedis(vehicleDto);
            logger.debug("quantityInDB: {} quantityInRedis: {}", quantityInDB, quantityInRedis);
            logger.debug("-------------------");
            if(quantityInDB > quantityInRedis){
                redisVehicleService.saveData(key,vehicleDto);
                return true;
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if(locked && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }

    private String getKeyFromVehicleDto(VehicleHoldDto vehicleDto) {
        return String.format("%s:%d",vehicleDto.getEmail(),vehicleDto.getVehicleId());
    }

    @Override
    public boolean cancelVehicle(VehicleHoldDto vehicleDto) {
        redisVehicleService.deleteData(getKeyFromVehicleDto(vehicleDto));
        return true;
    }

    @Override
    public boolean checkVehicle(BookingDto bookingDto) {
        List<Integer> choosedCar = bookingDto.getBookingCars().stream().map(BookingCarsRequestDto::getId).toList();
        if(!choosedCar.isEmpty()){
            for(int id : choosedCar){
                VehicleHoldDto vehicleRequestDto = new VehicleHoldDto(bookingDto.getUserEmail(),id);
                String key = getKeyFromVehicleDto(vehicleRequestDto);
                logger.debug("key {}", key);
                logger.debug("key in redis :{}", redisTemplate.hasKey(key));
                if(!redisTemplate.hasKey(key)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Map<String,String> getAllCarTypes() {
        return CarType.getValues();
    }

    @Override
    public PageResponseDto<List<VehicleDto>> getAllVehiclesByFilter(FilterCarAdminDto filterDto) throws ParseException {
        StringBuilder builder = new StringBuilder("SELECT * FROM vehicles WHERE 1=1");
        Map<String, Object> params = new HashMap<>();
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        if(!filterDto.getCarStatus().equals("0")){
            builder.append(" AND status=:carStatus");
            params.put("carStatus",filterDto.getCarStatus());
        }
        if(!filterDto.getCarType().equals("0")){
            builder.append(" AND car_type=:carType");
            params.put("carType",filterDto.getCarType());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!filterDto.getTimeOption().equals("0")){
            builder.append(" AND created_at >=:beginDate AND created_at <=:endDate");
            LocalDate now = LocalDate.now();
            switch (filterDto.getTimeOption()) {
                case "custom":{
                    Date beginDate = sdf.parse(filterDto.getBeginDate());
                    Date endDate = sdf.parse(filterDto.getEndDate());
                    params.put("beginDate",beginDate);
                    params.put("endDate",endDate);
                    break;
                }
                case "today": {
                    LocalDate today = LocalDate.now();
                    params.put("beginDate", today.atStartOfDay());
                    params.put("endDate", today.plusDays(1).atStartOfDay());
                    break;
                }
                case "yesterday": {
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    params.put("beginDate", yesterday.atStartOfDay());
                    params.put("endDate", yesterday.plusDays(1).atStartOfDay());
                    break;
                }
                case "last_7_days": {
                    params.put("beginDate", now.minusDays(7).atTime(LocalTime.MIN));
                    params.put("endDate", now.atTime(LocalTime.MAX));
                    break;
                }
                case "last_30_days": {
                    params.put("beginDate", now.minusDays(30).atTime(LocalTime.MIN));
                    params.put("endDate", now.atTime(LocalTime.MAX));
                    break;
                }
                case "this_week": {
                    LocalDate today = LocalDate.now();
                    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                    LocalDate endOfWeek = startOfWeek.plusDays(7);
                    params.put("beginDate", startOfWeek.atStartOfDay());
                    params.put("endDate", endOfWeek.atStartOfDay());
                    break;
                }
                case "this_month": {
                    LocalDate today = LocalDate.now();
                    LocalDate firstDay = today.withDayOfMonth(1);
                    LocalDate firstOfNextMonth = firstDay.plusMonths(1);
                    params.put("beginDate", firstDay.atStartOfDay());
                    params.put("endDate", firstOfNextMonth.atStartOfDay());
                    break;
                }
                case "this_year": {
                    LocalDate today = LocalDate.now();
                    LocalDate firstDay = today.withDayOfYear(1);
                    LocalDate firstOfNextYear = firstDay.plusYears(1);
                    params.put("beginDate", firstDay.atStartOfDay());
                    params.put("endDate", firstOfNextYear.atStartOfDay());
                    break;
                }
            }
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString().replace("SELECT *","SELECT COUNT(*)"),Long.class);
        if(!filterDto.getSortOption().equals("0")){
            switch (filterDto.getSortOption()) {
                case "quantity_desc":
                    builder.append(" ORDER BY quantity DESC, id ASC ");
                    break;
                case "quantity_asc":
                    builder.append(" ORDER BY quantity ASC, id ASC ");
                    break;
                case "price_desc":
                    builder.append(" ORDER BY price DESC, id ASC ");
                    break;
                case "price_asc":
                    builder.append(" ORDER BY price ASC, id ASC ");
                    break;
                case "date_desc":
                    builder.append(" ORDER BY created_at DESC, id ASC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY created_at ASC, id ASC ");
                    break;
                default:
                    break;
            }
        }
        else{
            builder.append(" ORDER BY id ASC ");
        }
        Query query = entityManager.createNativeQuery(builder.toString(), Vehicles.class);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        List<Vehicles> vehicles = (List<Vehicles>) query.getResultList();
        return PageResponseDto.<List<VehicleDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(vehicles.stream().map(VehicleConverter::toVehicleDto).toList())
                .build();
    }

    @Override
    public PageResponseDto<List<VehicleDto>> getSearchVehicles(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder("SELECT * FROM vehicles WHERE 1=1 ");
        if(keyword != null && !keyword.isEmpty()){
            builder.append(" AND ( (CAST(id AS TEXT) ILIKE :keyword)");
            builder.append(" OR (unaccent(license_plate) ILIKE unaccent(:keyword))");
            builder.append(" OR (unaccent(car_type) ILIKE unaccent(:keyword)) )");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString()
                .replace("SELECT *", "SELECT COUNT(*)"), Long.class);
        builder.append(" ORDER BY id ASC ");
        Query query = entityManager.createNativeQuery(builder.toString(), Vehicles.class);
        if(keyword != null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword+'%');
            queryTotal.setParameter("keyword", '%'+keyword+'%');
        }
        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        long total = ((Number) queryTotal.getSingleResult()).longValue();
        List<Vehicles> vehicles = (List<Vehicles>) query.getResultList();
        return PageResponseDto.<List<VehicleDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(vehicles.stream().map(VehicleConverter::toVehicleDto).toList())
                .build();
    }

    private Vehicles getVehicleEntityById(Integer id){
        return vehiclesRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Vehicle","id",String.valueOf(id)));
    }
    @Override
    public VehicleDto getVehicleById(Integer id) {
        Vehicles vehicles = getVehicleEntityById(id);
        return VehicleConverter.toVehicleDto(vehicles);
    }

    @Override
    public VehicleDto updateVehicle(Integer id, VehicleRequestDto vehicleDto, MultipartFile file) {
        Vehicles existVehicle = vehiclesRepository.findByLicensePlate(vehicleDto.getLicensePlate());
        if(existVehicle != null && !existVehicle.getId().equals(id)){
            throw new ResourceAlreadyExistsException("Vehicle","LicensePlate",vehicleDto.getLicensePlate());
        }
        Vehicles vehicles = getVehicleEntityById(id);
        vehicles.setQuantity(vehicleDto.getQuantity());
        vehicles.setPrice(vehicleDto.getPrice());
        vehicles.setImages(uploadCloudinary.uploadCloudinary(file));
        vehicles.setDiscount(vehicleDto.getDiscount());
        vehicles.setCarType(CarType.valueOf(vehicleDto.getCarType()));
        vehicles.setStar(vehicleDto.getStar());
        vehicles.setStatus(CarStatus.valueOf(vehicleDto.getStatus()));
        return VehicleConverter.toVehicleDto(vehiclesRepository.save(vehicles));
    }

    @Override
    public Map<String, String> getAllCarStatus() {
        return CarStatus.getValues();
    }

    @Override
    public VehicleDto createVehicle(VehicleRequestDto vehicleDto, MultipartFile file) {
        Vehicles existVehicle = vehiclesRepository.findByLicensePlate(vehicleDto.getLicensePlate());
        if(existVehicle!=null){
            throw new ResourceAlreadyExistsException("Vehicle","LicensePlate",vehicleDto.getLicensePlate());
        }
        Vehicles vehicles = Vehicles.builder()
                .licensePlate(vehicleDto.getLicensePlate())
                .price(vehicleDto.getPrice())
                .images(uploadCloudinary.uploadCloudinary(file))
                .discount(vehicleDto.getDiscount())
                .status(CarStatus.valueOf(vehicleDto.getStatus()))
                .carType(CarType.valueOf(vehicleDto.getCarType()))
                .star(vehicleDto.getStar())
                .quantity(vehicleDto.getQuantity())
                .build();
        return VehicleConverter.toVehicleDto(vehiclesRepository.save(vehicles));
    }


    private int totalQuantityInDB(VehicleHoldDto vehicleDto) {
        Vehicles vehicles = vehiclesRepository.findById(vehicleDto.getVehicleId()).
                orElseThrow(()-> new ResourceNotFoundException("Vehicle","id",String.valueOf(vehicleDto.getVehicleId())));
        return vehicles.getQuantity();
    }
    private int totalQuantityInRedis(VehicleHoldDto vehicleDto) {
        String parttern = String.format("*:%d", vehicleDto.getVehicleId());
        Set<String> keys = redisTemplate.keys(parttern);
        logger.debug("keys: {}", keys);
        String currentKey = getKeyFromVehicleDto(vehicleDto);
        int total =0;
        for (String key : keys) {
            logger.debug("key in for: {}", key);
            if(currentKey.equals(key)) {
                continue;
            }
            int quantity = redisVehicleService.getData(key);
            total += quantity;
        }
        return total;
    }
}
