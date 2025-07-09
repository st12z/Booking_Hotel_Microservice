package com.thuc.bookings.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.service.IRedisBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisBookingServiceImpl implements IRedisBookingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void saveData(String key, BookingDto bookingDto) {
        try{
            String json = objectMapper.writeValueAsString(bookingDto);
            redisTemplate.opsForValue().set(key, json,5, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookingDto getData(String key) {
        try{
            String json = (String) redisTemplate.opsForValue().get(key);
            return json!=null?objectMapper.readValue(json, BookingDto.class):null;
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
