package com.thuc.rooms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.dto.CheckRoomDto;
import com.thuc.rooms.service.IRedisHoldRooms;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisHoldRoomsImpl implements IRedisHoldRooms {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void saveData(String key, CheckRoomDto checkRoomDto) {
        try{
            String json = objectMapper.writeValueAsString(checkRoomDto.getQuantity());
            redisTemplate.opsForValue().set(key,json,5, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Integer getData(String key) {
        try{
            String json = (String) redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(json, Integer.class);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteData( String key) {
        try{
            if(redisTemplate.hasKey(key)){
                redisTemplate.delete(key);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
