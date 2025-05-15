package com.thuc.rooms.service;

import com.thuc.rooms.dto.RoomChatsDto;

import java.util.List;

public interface IRoomChatService {
    List<RoomChatsDto> getRoomChatsOfUser(int userId);

    RoomChatsDto createRoomChats(RoomChatsDto roomChatsDto);

    RoomChatsDto getRoomChatsById(int id);
}
