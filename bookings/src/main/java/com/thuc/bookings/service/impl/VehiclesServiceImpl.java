package com.thuc.bookings.service.impl;

import com.thuc.bookings.converter.VehicleConverter;
import com.thuc.bookings.dto.requestDto.FilterDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.entity.Vehicles;
import com.thuc.bookings.exception.ResourceNotFoundException;
import com.thuc.bookings.repository.VehiclesRepository;
import com.thuc.bookings.service.IRedisVehicleService;
import com.thuc.bookings.service.IVehiclesService;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiclesServiceImpl implements IVehiclesService {
    private final VehiclesRepository vehiclesRepository;
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final IRedisVehicleService redisVehicleService;
    private final Logger logger = LoggerFactory.getLogger(VehiclesServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VehicleDto> getAllVehicles(FilterDto filterDto) {
        StringBuilder builder = new StringBuilder("SELECT * FROM vehicles WHERE 1=1 ");
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
        logger.debug("query: {}", query);
        List<Vehicles> vehicles =(List<Vehicles>) query.getResultList();
        return vehicles.stream().map(VehicleConverter::toVehicleDto).toList();
    }

    @Override
    public boolean holdVehicle(VehicleRequestDto vehicleDto) {
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

    private String getKeyFromVehicleDto(VehicleRequestDto vehicleDto) {
        return String.format("%s:%d",vehicleDto.getEmail(),vehicleDto.getVehicleId());
    }

    @Override
    public boolean cancelVehicle(VehicleRequestDto vehicleDto) {
        redisVehicleService.deleteData(getKeyFromVehicleDto(vehicleDto));
        return true;
    }
    private int totalQuantityInDB(VehicleRequestDto vehicleDto) {
        Vehicles vehicles = vehiclesRepository.findById(vehicleDto.getVehicleId()).
                orElseThrow(()-> new ResourceNotFoundException("Vehicle","id",String.valueOf(vehicleDto.getVehicleId())));
        return vehicles.getQuantity();
    }
    private int totalQuantityInRedis(VehicleRequestDto vehicleDto) {
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
