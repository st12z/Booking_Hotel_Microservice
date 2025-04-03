package com.thuc.rooms.service;

import com.thuc.rooms.dto.RoomTypeDto;

import java.util.List;

public interface IRoomTypeService {
    List<RoomTypeDto> getAllRoomTypes(String slugProperty);
}
