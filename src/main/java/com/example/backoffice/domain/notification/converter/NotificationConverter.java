package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;

public class NotificationConverter {

    public static Notification toEntity(
            String toMemberName, String fromMemberName, String message){
        return Notification.builder()
                .toMemberName(toMemberName)
                .fromMemberName(fromMemberName)
                .message(message)
                .build();
    }

    public static NotificationResponseDto.CreateNotificationResponseDto toCreateNotificationDto(
            Notification notification){
        return NotificationResponseDto.CreateNotificationResponseDto.builder()
                .toMemberName(notification.getToMemberName())
                .fromMemberName(notification.getFromMemberName())
                .message(notification.getMessage())
                .build();
    }
}
