package com.example.backoffice.domain.notification.facade;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationsServiceFacade {
    void createNotification(
            NotificationData notificationData, NotificationType domainType);

    NotificationsResponseDto.ReadNotificationResponseDto readOne(
            Long memberId, String notificationId, Members member);

    NotificationsResponseDto.CreateNotificationListResponseDto createAdminNotification(
            Long adminId, Members member,
            NotificationsRequestDto.CreateNotificationRequestDto requestDto);

    Page<NotificationsResponseDto.ReadNotificationListResponseDto> readList(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationsResponseDto.ReadNotificationListResponseDto> readUnreadList(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationsResponseDto.ReadNotificationListResponseDto> readReadList(
            Long memberId, Members member, Pageable pageable);
    void deleteNotification(
            Long memberId, NotificationsRequestDto.DeleteNotificationRequestDto requestDto,
            Members member);

    List<NotificationsResponseDto.ReadNotificationListResponseDto> readAll(
            Long memberId, Members member);
}
