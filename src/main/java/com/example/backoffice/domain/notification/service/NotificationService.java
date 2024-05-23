package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationRequestDto;
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

    NotificationResponseDto.CreateNotificationListResponseDto createAdminNotification(
            Long adminId, Members member,
            NotificationRequestDto.CreateNotificationRequestDto requestDto);

    void deleteNotification(
            Long memberId, List<String> notificationIds, Members member);

    Notification findById(String notificationId);
}
