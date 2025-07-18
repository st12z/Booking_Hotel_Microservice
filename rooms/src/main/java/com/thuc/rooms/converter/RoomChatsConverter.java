package com.thuc.rooms.converter;

import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.entity.RoomChats;

public class RoomChatsConverter {
    public static RoomChatsDto toRoomChatsDto(RoomChats roomChats) {
        return RoomChatsDto.builder()
                .id(roomChats.getId())
                .userAId(roomChats.getUserAId())
                .userBId(roomChats.getUserBId())
                .build();
    }

}
