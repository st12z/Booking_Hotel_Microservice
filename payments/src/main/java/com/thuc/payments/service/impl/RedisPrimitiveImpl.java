package com.thuc.payments.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.service.IRedisPrimitive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisPrimitiveImpl implements IRedisPrimitive {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void saveData(String key, Object id) {
        try{
            String json = objectMapper.writeValueAsString(id);
            redisTemplate.opsForValue().set(key, json,10, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getData(String key, Class<T> clazz) {
        try {
            String json = (String) redisTemplate.opsForValue().get(key);
            return json != null ? objectMapper.readValue(json, clazz) : null;
        } catch (Exception e) {
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
