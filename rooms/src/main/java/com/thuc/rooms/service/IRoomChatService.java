package com.thuc.rooms.service;

import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.RoomChatRequestDto;
import com.thuc.rooms.dto.RoomChatsDto;

import java.util.List;

public interface IRoomChatService {
    List<RoomChatsDto> getRoomChatsOfUser(int userId);

    RoomChatsDto createRoomChats(RoomChatRequestDto roomChatsDto);

    RoomChatsDto getRoomChatsById(int id);

    PageResponseDto<List<RoomChatsDto>> getAllRoomChats(String keyword,Integer pageNo, Integer pageSize);

    List<RoomChatsDto> updateRoomChats(List<RoomChatsDto> roomChatsDto);
}
