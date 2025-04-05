package com.thuc.rooms.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.service.IRedisDestinationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisDestinationServiceImpl implements IRedisDestinationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(RedisDestinationServiceImpl.class);
    @Override
    public void saveData(String key, List<Object> value) {
        logger.debug("Saving data to redis");
        try{
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json,1, TimeUnit.DAYS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Object> getData(String key) {
        logger.debug("Getting data to redis");
        try{
            String json = (String) redisTemplate.opsForValue().get(key);
            if(json!=null){
                return objectMapper.readValue(json, new TypeReference<List<Object>>() {});
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
