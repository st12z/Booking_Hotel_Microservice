package com.thuc.rooms.service;

import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface IRoomTypeService {
    List<RoomTypeDto> getAllRoomTypes(String slugProperty);

    List<RoomTypeDto> getAllRoomTypesBySearch(String slugProperty,SearchDto searchDto);
}
