package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.ChatConverter;
import com.thuc.rooms.converter.RoomChatsConverter;
import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.entity.Chats;
import com.thuc.rooms.entity.RoomChats;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.ChatRepository;
import com.thuc.rooms.repository.RoomChatsRepository;
import com.thuc.rooms.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final ChatRepository chatRepository;
    private final RoomChatsRepository roomChatsRepository;
    @Override
    public List<RoomChatsDto> getRoomChatsOfUser(int userId) {
        List<RoomChats> roomChats = roomChatsRepository.findByUserAIdOrUserBId(userId,userId);
        return roomChats.stream().map(RoomChatsConverter::toRoomChatsDto).collect(Collectors.toList());
    }

    @Override
    public List<ChatResponseDto> getChatsByRoomChatId(int roomChatId) {
        RoomChats roomChats=getRoomChatsById(roomChatId);
        return roomChats.getChats().stream().map(ChatConverter::toChatResponseDto).collect(Collectors.toList());

    }
    public RoomChats getRoomChatsById(int roomChatId) {
        return roomChatsRepository.findById(roomChatId).orElseThrow(()-> new ResourceNotFoundException("RoomChats","id",String.valueOf(roomChatId)));

    }
    @Override
    public ChatResponseDto createMessage(ChatResponseDto chatRequest) {
        Chats chat = Chats.builder()
                .content(chatRequest.getContent())
                .images(chatRequest.getImages())
                .userSend(chatRequest.getUserSend())
                .roomChats(getRoomChatsById(chatRequest.getRoomChatId()))
                .build();
        System.out.println(chat.getId());
        Chats saveChat = chatRepository.save(chat);
        return ChatConverter.toChatResponseDto(saveChat);
    }
}
