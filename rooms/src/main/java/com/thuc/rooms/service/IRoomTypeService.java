package com.thuc.rooms.service;

import com.thuc.rooms.dto.CheckRoomDto;
import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.dto.SearchDto;

import java.util.List;

public interface IRoomTypeService {
    List<RoomTypeDto> getAllRoomTypes(String slugProperty);

    List<RoomTypeDto> getAllRoomTypesBySearch(String slugProperty,SearchDto searchDto);

    Integer checkEnoughRooms(CheckRoomDto checkRoomDto);

    RoomTypeDto getRoomTypeById(Integer id);

    boolean holdRooms(List<CheckRoomDto> roomReversed);
}
