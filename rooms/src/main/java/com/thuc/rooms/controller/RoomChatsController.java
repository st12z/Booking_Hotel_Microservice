package com.thuc.rooms.controller;

import com.thuc.rooms.constants.ChatConstant;
import com.thuc.rooms.constants.RoomChatConstant;
import com.thuc.rooms.dto.RoomChatsDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IRoomChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-chats")
@RequiredArgsConstructor
public class RoomChatsController {
    private final Logger logger = LoggerFactory.getLogger(RoomChatsController.class);
    private final IRoomChatService roomChatService;
    @GetMapping("/rooms/{userId}")
    public ResponseEntity<SuccessResponseDto<List<RoomChatsDto>>> getRoomChats(@PathVariable int userId) {
        logger.debug("Getting room chats of {}", userId);
        SuccessResponseDto<List<RoomChatsDto>> response = SuccessResponseDto.<List<RoomChatsDto>>builder()
                .code(ChatConstant.STATUS_200)
                .message(ChatConstant.MESSAGE_200)
                .data(roomChatService.getRoomChatsOfUser(userId))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<RoomChatsDto>> createRoomChat(@RequestBody RoomChatsDto roomChatsDto) {
        logger.debug("Creating room chats of {}", roomChatsDto);
        SuccessResponseDto<RoomChatsDto> response = SuccessResponseDto.<RoomChatsDto>builder()
                .code(RoomChatConstant.STATUS_200)
                .message(RoomChatConstant.MESSAGE_200)
                .data(roomChatService.createRoomChats(roomChatsDto))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<RoomChatsDto>> getRoomChat(@PathVariable int id) {
        logger.debug("Getting room chats of {}", id);
        SuccessResponseDto<RoomChatsDto> response = SuccessResponseDto.<RoomChatsDto>builder()
                .code(RoomChatConstant.STATUS_200)
                .message(RoomChatConstant.MESSAGE_200)
                .data(roomChatService.getRoomChatsById(id))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
