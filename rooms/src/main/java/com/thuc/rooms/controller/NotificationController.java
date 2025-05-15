package com.thuc.rooms.controller;

import com.thuc.rooms.constants.NotificationConstant;
import com.thuc.rooms.dto.NotificationDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationService notificationService;
    private final Logger log = LoggerFactory.getLogger(NotificationController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<NotificationDto>>> getAllNotifications(){
        log.debug("getAllNotifications");
        SuccessResponseDto<List<NotificationDto>> response = SuccessResponseDto.<List<NotificationDto>>builder()
                .code(NotificationConstant.STATUS_200)
                .message(NotificationConstant.MESSAGE_200)
                .data(notificationService.getAllNotifications())
                .build();
        return ResponseEntity.ok(response);
    }
}
