package com.thuc.rooms.service.impl;

import com.thuc.rooms.dto.FilterDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SearchDto;
import com.thuc.rooms.service.IFilterService;
import com.thuc.rooms.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements IFilterService {
    private final IRedisService redisService;

    @Override
    public PageResponseDto<List<PropertyDto>> filterByCondition(SearchDto searchDto, FilterDto filter) {
        return null;
    }
}
