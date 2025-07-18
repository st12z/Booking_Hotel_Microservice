package com.thuc.rooms.controller;

import com.thuc.rooms.dto.ChatResponseDto;
import com.thuc.rooms.dto.NotificationDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.UserDto;
import com.thuc.rooms.service.IChatService;
import com.thuc.rooms.service.INotificationService;
import com.thuc.rooms.service.client.UsersFeignClient;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationsWebSocket {
    private final INotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Logger log = LoggerFactory.getLogger(NotificationsWebSocket.class);
    private final UsersFeignClient usersFeignClient;
    private List<UserDto> getAllUserDtos(){
        SuccessResponseDto<List<UserDto>> response = usersFeignClient.getAllUsersAdmin().getBody();
        return response.getData();
    }
    @MessageMapping("/sendNotification")
    public NotificationDto sendNotification(@Payload NotificationDto notificationDto) {
        try{
            List<UserDto> userDtos = getAllUserDtos();
            NotificationDto notificationReturn = notificationService.save(notificationDto);
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/messages", notificationReturn);
            }
            return notificationReturn;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @MessageMapping("/sendUpdateVisits")
    public Integer sendUpdateVisit(@Payload Integer updateVisits) {
        try{
            List<UserDto> userDtos = getAllUserDtos();
            log.debug("sendUpdateVisit :{}", updateVisits);
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/update-visits", updateVisits);
            }
            return updateVisits;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @MessageMapping("/sendAmountUsers")
    public void sendAmountUsers(@Payload String message) {
        try{
            List<UserDto> userDtos = getAllUserDtos();
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/amount-users", message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendAmountBillsToday")
    public void sendAmountBills(@Payload Integer count) {
        try{
            List<UserDto> userDtos = getAllUserDtos();
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/amount-bills-today", count);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendAmountReviews")
    public void sendAmountReviews(@Payload String message) {
        try{
            List<UserDto> userDtos = getAllUserDtos();
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/amount-reviews", message);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendAmountRevenueToday")
    public void sendAmountRevenueToday(@Payload Integer amountRevenueToday) {
        try{
            List<UserDto> userDtos = getAllUserDtos();
            log.debug("sendAmountRevenueToday :{}", amountRevenueToday);
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/amount-revenue-today", amountRevenueToday);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @MessageMapping("/sendNotifyMessage")
    public void sendNotifyMessage(@Payload String message) {
        try{

            log.debug("sendNotifyMessage :{}", message);
            List<UserDto> userDtos = getAllUserDtos();
            for (UserDto userDto : userDtos) {
                messagingTemplate.convertAndSendToUser(userDto.getEmail(), "/queue/notifymessage", message);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}