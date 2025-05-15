package com.thuc.rooms.service;

import com.thuc.rooms.dto.NotificationDto;

import java.util.List;

public interface INotificationService {
    List<NotificationDto> getAllNotifications();

    NotificationDto save(NotificationDto notificationDto);
}
