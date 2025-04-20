package com.thuc.rooms.service;


import com.thuc.rooms.dto.CheckRoomDto;

import java.util.List;

public interface IRedisHoldRooms {
    public void saveData(String key, CheckRoomDto checkRoomDto);
    public Integer getData(String key);

    void deleteData(String key);
}
