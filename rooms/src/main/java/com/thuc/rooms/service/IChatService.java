package com.thuc.rooms.service;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.RoomChatsDto;

import java.util.List;

public interface IChatService {


    List<ChatResponseDto> getChatsByRoomChatId(int roomChatId);

    ChatResponseDto createMessage(ChatResponseDto chatRequest);

    PageResponseDto<List<ChatResponseDto>> getAllMessagesPage(int roomChatId, Integer userId, Integer pageNo, Integer pageSize);
}
