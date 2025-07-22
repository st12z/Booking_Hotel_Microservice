package com.thuc.rooms.controller;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.UserDto;
import com.thuc.rooms.service.IChatService;
import com.thuc.rooms.service.client.UsersFeignClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessagesController {
    private final Logger log = LoggerFactory.getLogger(MessagesController.class);
    private final IChatService chatService;
    private final UsersFeignClient usersFeignClient;
    @MessageMapping("/sendMessage/{roomChatId}")
    @SendTo("/topic/rooms/{roomChatId}")
    public ChatResponseDto sendMessage(@DestinationVariable Integer roomChatId, @Payload ChatResponseDto chatRequest) {
        try{
            ChatResponseDto responseDto = chatService.createMessage(chatRequest);
            return responseDto;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
