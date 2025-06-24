package com.thuc.rooms.converter;

import com.thuc.rooms.dto.RoomDto;
import com.thuc.rooms.entity.Room;

public class RoomConverter {
    public static RoomDto toRoomDto(Room room){
        return RoomDto.builder()
                .id(room.getId())
                .roomType(RoomTypeConverter.toRoomTypDto(room.getRoomType()))
                .roomNumber(room.getRoomNumber())
                .status(room.getStatus())
                .build();
    }
}
