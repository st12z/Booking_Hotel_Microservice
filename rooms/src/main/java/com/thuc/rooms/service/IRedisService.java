package com.thuc.rooms.service;

public interface IRedisService {
    public void saveData(String key, Object value);
    public Object getData(String key);
}
