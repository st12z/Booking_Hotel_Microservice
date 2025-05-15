package com.thuc.rooms.converter;

import com.thuc.rooms.dto.NotificationDto;
import com.thuc.rooms.entity.Notifications;

import javax.management.Notification;

public class NotificationConverter {
    public static NotificationDto toDto(Notifications notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .build();
    }
    public static Notifications toEntity(NotificationDto notification) {
        return Notifications.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .build();
    }
}
