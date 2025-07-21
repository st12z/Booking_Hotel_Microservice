package com.thuc.rooms.controller;

import com.thuc.rooms.constants.ChatConstant;
import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/chats")
@RestController
@RequiredArgsConstructor
public class ChatsController {
    private final IChatService chatService;
    private final Logger logger = LoggerFactory.getLogger(ChatsController.class);

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
    @GetMapping("/messages-page/{roomChatId}")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<ChatResponseDto>>>> getMessagesPage(
            @PathVariable int roomChatId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId

    ) {
        logger.debug("getting messages of roomChatId {} and {}",roomChatId,userId);
        SuccessResponseDto<PageResponseDto<List<ChatResponseDto>>> response = SuccessResponseDto.<PageResponseDto<List<ChatResponseDto>>>builder()
                .code(ChatConstant.STATUS_200)
                .message(ChatConstant.MESSAGE_200)
                .data(chatService.getAllMessagesPage(roomChatId,userId,pageNo,pageSize))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
