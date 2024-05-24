package com.example.backoffice.domain.notification.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto.CreateNotificationResponseDto createNotification(
            NotificationData notificationData, NotificationType domainType);

    NotificationResponseDto.ReadNotificationResponseDto readOne(
            Long memberId, String notificationId, Members member);

    NotificationResponseDto.CreateNotificationListResponseDto createAdminNotification(
            Long adminId, Members member,
            NotificationRequestDto.CreateNotificationRequestDto requestDto);

    Page<NotificationResponseDto.ReadNotificationListResponseDto> readList(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationResponseDto.ReadNotificationListResponseDto> readUnreadList(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationResponseDto.ReadNotificationListResponseDto> readReadList(
            Long memberId, Members member, Pageable pageable);
    void deleteNotification(
            Long memberId, List<String> notificationIds, Members member);

    Notification findById(String notificationId);
}
