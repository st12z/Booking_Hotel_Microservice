package com.thuc.messages.service;

public interface IRedisPrimitive {
    <T> void saveData(String key, T data);
    <T> T getData(String key, Class<T> clazz); // cần truyền class để deserialize
    void deleteData(String key);
}