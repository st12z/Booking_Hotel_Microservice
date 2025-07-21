package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.ChatConverter;
import com.thuc.rooms.converter.RoomChatsConverter;
import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.entity.Chats;
import com.thuc.rooms.entity.RoomChats;
import com.thuc.rooms.exception.ResourceNotFoundException;
import com.thuc.rooms.repository.ChatRepository;
import com.thuc.rooms.repository.RoomChatsRepository;
import com.thuc.rooms.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final ChatRepository chatRepository;
    private final RoomChatsRepository roomChatsRepository;


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

    @Override
    public PageResponseDto<List<ChatResponseDto>> getAllMessagesPage(int roomChatId, Integer userId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(Sort.Direction.ASC,"id"));
        Page<Chats> chats = userId!=null ? chatRepository.findByRoomChatIdAndUserId(roomChatId,userId,pageable)
                : chatRepository.findByRoomChatId(roomChatId,pageable);
        return PageResponseDto.<List<ChatResponseDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(chats.getTotalElements())
                .dataPage(chats.getContent().stream().map(ChatConverter::toChatResponseDto).collect(Collectors.toList()))
                .build();
    }
}
