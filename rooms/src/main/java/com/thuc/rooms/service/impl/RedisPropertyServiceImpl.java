package com.thuc.rooms.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.service.IRedisPropertyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisPropertyServiceImpl implements IRedisPropertyService {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(RedisPropertyServiceImpl.class);
    @Override
    public void saveData(String key, List<PropertyDto> value) {
        try{
            logger.debug("Saving data to redis...");
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json,1,TimeUnit.HOURS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<PropertyDto> getData(String key) {
        try{
            logger.debug("Getting data to redis...");
            String json =(String) redisTemplate.opsForValue().get(key);
            if(json == null){
                return null;
            }
            return objectMapper.readValue(json, new TypeReference<List<PropertyDto>>() {});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
