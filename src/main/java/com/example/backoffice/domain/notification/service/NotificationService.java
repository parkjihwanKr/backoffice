package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;

public interface NotificationService {

    NotificationResponseDto.CreateNotificationResponseDto createNotification(
            Long memberId, Members member);

    NotificationResponseDto.ReadNotificationResponseDto readOne(
            Long memberId, String notificationId, Members member);
}
