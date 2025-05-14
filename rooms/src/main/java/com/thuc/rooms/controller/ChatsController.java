package com.thuc.rooms.controller;

import com.thuc.rooms.constants.ChatConstant;
import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/chats")
@RestController
@RequiredArgsConstructor
public class ChatsController {
    private final IChatService chatService;
    private final Logger logger = LoggerFactory.getLogger(ChatsController.class);
    @GetMapping("/rooms/{userId}")
    public ResponseEntity<SuccessResponseDto<List<RoomChatsDto>>> getRoomChats(@PathVariable int userId) {
        logger.debug("Getting room chats of {}", userId);
        SuccessResponseDto<List<RoomChatsDto>> response = SuccessResponseDto.<List<RoomChatsDto>>builder()
                .code(ChatConstant.STATUS_200)
                .message(ChatConstant.MESSAGE_200)
                .data(chatService.getRoomChatsOfUser(userId))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/messages/{roomChatId}")
    public ResponseEntity<SuccessResponseDto<List<ChatResponseDto>>> getMessages(@PathVariable int roomChatId) {
        logger.debug("Getting messages of {}", roomChatId);
        SuccessResponseDto<List<ChatResponseDto>> response = SuccessResponseDto.<List<ChatResponseDto>>builder()
                .code(ChatConstant.STATUS_200)
                .message(ChatConstant.MESSAGE_200)
                .data(chatService.getChatsByRoomChatId(roomChatId))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
