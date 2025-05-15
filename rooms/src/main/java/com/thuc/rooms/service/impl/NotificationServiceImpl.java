package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.NotificationConverter;
import com.thuc.rooms.dto.NotificationDto;
import com.thuc.rooms.entity.Notifications;
import com.thuc.rooms.repository.NotificationRepository;
import com.thuc.rooms.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private final NotificationRepository notificationRepository;
    @Override
    public List<NotificationDto> getAllNotifications() {
        List<Notifications> notifications = notificationRepository.findAll().stream().sorted(
                Comparator.comparing(Notifications::getCreatedAt).reversed()
        ).toList();
        return notifications.stream().map(NotificationConverter::toDto).toList();
    }

    @Override
    public NotificationDto save(NotificationDto notificationDto) {
        Notifications notification = NotificationConverter.toEntity(notificationDto);
        notification = notificationRepository.save(notification);
        return NotificationConverter.toDto(notification);
    }
}
