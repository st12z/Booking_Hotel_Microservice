package com.thuc.rooms.service;

import com.thuc.rooms.dto.PropertyDto;

import java.util.List;

public interface IRedisPropertyService {
    public void saveData(String key, List<PropertyDto> value);
    public List<PropertyDto> getData(String key);
}
