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
    void createOne(NotificationData notificationData, NotificationType domainType);

    NotificationsResponseDto.ReadOneDto readOne(
            Long memberId, String notificationId, Members member);

    NotificationsResponseDto.CreateOneForAdminDto createOneForAdmin(
            Long adminId, Members member,
            NotificationsRequestDto.CreateOneForAdminDto requestDto);

    Page<NotificationsResponseDto.ReadAllDto> readAll(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationsResponseDto.ReadAllDto> readUnread(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationsResponseDto.ReadAllDto> readRead(
            Long memberId, Members member, Pageable pageable);
    void delete(Long memberId, NotificationsRequestDto.DeleteDto requestDto, Members member);

    List<NotificationsResponseDto.ReadAllDto> readAllForUnread(Long memberId, Members member);
}
