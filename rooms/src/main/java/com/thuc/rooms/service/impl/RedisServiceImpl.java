package com.thuc.rooms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements IRedisService {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Override
    public void saveData(String key, Object value) {
        try{
            logger.debug("Saving data to redis...");
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json,5, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object getData(String key) {
        try{
            logger.debug("Getting data to redis...");
            String json =(String) redisTemplate.opsForValue().get(key);
            return objectMapper.readValue(json,Object.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
