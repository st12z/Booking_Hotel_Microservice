package com.thuc.rooms.controller;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class MessagesController {
    private final IChatService chatService;
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
