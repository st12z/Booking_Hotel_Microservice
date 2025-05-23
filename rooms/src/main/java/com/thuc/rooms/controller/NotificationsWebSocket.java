package com.thuc.rooms.controller;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.NotificationDto;
import com.thuc.rooms.service.IChatService;
import com.thuc.rooms.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(NotificationsWebSocket.class);
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
    @MessageMapping("/sendUpdateVisits")
    public Integer sendUpdateVisit(@Payload Integer updateVisits) {
        try{
            log.debug("sendUpdateVisit :{}", updateVisits);
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/update-visits", updateVisits);
            return updateVisits;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @MessageMapping("/sendAmountUsers")
    public void sendAmountUsers(@Payload String message) {
        try{
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/amount-users",message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendAmountBillsToday")
    public void sendAmountBills(@Payload Integer count) {
        try{
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/amount-bills-today", count);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendAmountReviews")
    public void sendAmountReviews(@Payload String message) {
        try{
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/amount-reviews", message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendAmountRevenueToday")
    public void sendAmountRevenueToday(@Payload Integer amountRevenueToday) {
        try{
            log.debug("sendAmountRevenueToday :{}", amountRevenueToday);
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/amount-revenue-today", amountRevenueToday);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendNotifyMessage")
    public void sendNotifyMessage(@Payload String message) {
        try{
            log.debug("sendNotifyMessage :{}", message);
            messagingTemplate.convertAndSendToUser("manager@gmail.com", "/queue/notifymessage", message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}