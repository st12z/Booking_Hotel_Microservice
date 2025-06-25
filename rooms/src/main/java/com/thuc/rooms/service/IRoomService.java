package com.thuc.rooms.service;


import com.thuc.rooms.dto.RoomDto;
import com.thuc.rooms.dto.RoomRequestDto;

public interface IRoomService {
    RoomDto createRoom(RoomRequestDto roomDto);

    Integer getQuantityRoomsByPropertyIdAndRoomTypeId(Integer propertyId, Integer roomTypeId);

    Integer deleteRoom(Integer id);
}
