package com.thuc.rooms.controller;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.NotificationDto;
import com.thuc.rooms.service.IChatService;
import com.thuc.rooms.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class NotificationsWebSocket {
    private final INotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/sendNotification")
    public NotificationDto sendNotification(@Payload NotificationDto notificationDto) {
        try{

            NotificationDto notificationReturn = notificationService.save(notificationDto);
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/messages", notificationReturn);
            return notificationReturn;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
