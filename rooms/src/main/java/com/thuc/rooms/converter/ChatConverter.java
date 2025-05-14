package com.thuc.rooms.converter;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.entity.Chats;

public class ChatConverter {
    public static ChatResponseDto toChatResponseDto(Chats chats) {
        return ChatResponseDto.builder()
                .id(chats.getId())
                .roomChatId(chats.getRoomChats().getId())
                .content(chats.getContent())
                .images(chats.getImages())
                .userSend(chats.getUserSend())
                .build();
    }

}
