package com.example.backoffice.domain.notification.facade;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationsServiceFacadeV1 {
    void createOne(NotificationData notificationData, NotificationType domainType);

    NotificationsResponseDto.ReadOneDto readOne(
            Long memberId, String notificationId, Members member);

    void createForAdmin(
            String memberName, NotificationsRequestDto.CreateForAdminDto requestDto);

    void createFilteredForAdmin(
            String memberName,
            NotificationsRequestDto.CreateFilteredForAdminDto requestDto);

    Page<NotificationsResponseDto.ReadDto> read(
            Long memberId, Members member, Pageable pageable);

    Page<NotificationsResponseDto.ReadDto> readUnread(
            Long memberId, Members loginMember, Pageable pageable);

    Page<NotificationsResponseDto.ReadDto> readRead(
            Long memberId, Members member, Pageable pageable);

    List<String> delete(
            Long memberId, NotificationsRequestDto.DeleteDto requestDto, Members member);

    List<NotificationsResponseDto.ReadAllDto> readAll(Long memberId, Members member);;
}