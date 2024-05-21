package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto.CreateNotificationResponseDto createNotification(
            NotificationData notificationData, NotificationType domainType);

    NotificationResponseDto.ReadNotificationResponseDto readOne(
            Long memberId, String notificationId, Members member);

    void deleteNotification(
            Long memberId, List<String> notificationIds, Members member);

    Notification findById(String notificationId);
}
