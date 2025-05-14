package com.thuc.rooms.service;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.RoomChatsDto;

import java.util.List;

public interface IChatService {
    List<RoomChatsDto> getRoomChatsOfUser(int userId);

    List<ChatResponseDto> getChatsByRoomChatId(int roomChatId);

    ChatResponseDto createMessage(ChatResponseDto chatRequest);
}
