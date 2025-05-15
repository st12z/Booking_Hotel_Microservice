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
    public static RoomChats toRoomChats(RoomChatsDto roomChatsDto) {
        return RoomChats.builder()
                .id(roomChatsDto.getId())
                .userAId(roomChatsDto.getUserAId())
                .userBId(70)
                .build();
    }
}
