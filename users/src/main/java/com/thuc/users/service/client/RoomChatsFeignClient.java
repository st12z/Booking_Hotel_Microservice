package com.thuc.users.service.client;

import com.thuc.users.dto.requestDto.RoomChatsDto;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "rooms",path = "/api/room-chats")
public interface RoomChatsFeignClient {
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<RoomChatsDto>> createRoomChat(@RequestBody RoomChatsDto roomChatsDto);
}

