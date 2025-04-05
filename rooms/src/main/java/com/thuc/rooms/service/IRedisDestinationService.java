package com.thuc.rooms.service;

import com.thuc.rooms.dto.PropertyDto;

import java.util.List;

public interface IRedisDestinationService {
    public void saveData(String key, List<Object> value);
    public List<Object> getData(String key);
}
