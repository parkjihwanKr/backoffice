package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationType;

public class NotificationConverter {

    public static Notification toEntity(
            String toMemberName, String fromMemberName, String message,
            NotificationType notificationType){
        return Notification.builder()
                .toMemberName(toMemberName)
                .fromMemberName(fromMemberName)
                .message(message)
                .notificationType(notificationType)
                .isRead(false)
                .build();
    }

    public static NotificationResponseDto.CreateNotificationResponseDto toCreateOneDto(
            Notification notification){
        return NotificationResponseDto.CreateNotificationResponseDto.builder()
                .toMemberName(notification.getToMemberName())
                .fromMemberName(notification.getFromMemberName())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationResponseDto.ReadNotificationResponseDto toReadOne(
            Notification notification){

        return NotificationResponseDto.ReadNotificationResponseDto.builder()
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
