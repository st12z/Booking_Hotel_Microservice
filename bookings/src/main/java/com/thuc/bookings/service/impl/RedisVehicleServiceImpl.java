package com.thuc.bookings.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.service.IRedisVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisVehicleServiceImpl implements IRedisVehicleService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void saveData(String key, VehicleRequestDto vehicleDto) {
        try{
            String json = objectMapper.writeValueAsString(1);
            redisTemplate.opsForValue().set(key, json,10, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getData(String key) {
        try{
            String json = (String) redisTemplate.opsForValue().get(key);
            if(json != null){
                return objectMapper.readValue(json, Integer.class);
            }
            return 0;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData(String key) {
        try{
            redisTemplate.delete(key);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
