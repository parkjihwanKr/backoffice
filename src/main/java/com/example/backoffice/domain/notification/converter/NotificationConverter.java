package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationType;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationConverter {

    public static Notification toEntity(
            String toMemberName, String fromMemberName, String message,
            NotificationType notificationType, MemberDepartment memberDepartment){
        return Notification.builder()
                .toMemberName(toMemberName)
                .fromMemberName(fromMemberName)
                .message(message)
                .notificationType(notificationType)
                .fromMemberDepartment(memberDepartment)
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

    public static NotificationResponseDto.ReadNotificationResponseDto toReadOneDto(
            Notification notification){

        return NotificationResponseDto.ReadNotificationResponseDto.builder()
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .fromMemberDepartment(notification.getFromMemberDepartment())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .message(notification.getMessage())
                .build();
    }

    public static NotificationResponseDto.CreateNotificationListResponseDto toCreateDto(
            Members mainAdmin, Set<MemberDepartment> memberDepartmentSet,
            List<Notification> notificationList, String message){
        List<String> toMemberNameList = new ArrayList<>();
        for (Notification notification : notificationList) {
            toMemberNameList.add(
                    notification.getToMemberName());
        }
        return NotificationResponseDto.CreateNotificationListResponseDto.builder()
                .message(message)
                .fromAdminRole(mainAdmin.getRole())
                .fromMemberName(mainAdmin.getMemberName())
                .toMemberDepartmentSet(memberDepartmentSet)
                .toMemberNameList(toMemberNameList)
                .build();
    }

    public static Page<NotificationResponseDto.ReadNotificationListResponseDto> toReadListDto(
            Page<Notification> notificationPage){
        return notificationPage.map(
                notification -> NotificationResponseDto.ReadNotificationListResponseDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build());
    }

    public static List<NotificationResponseDto.ReadNotificationListResponseDto> toReadAllDto(
            List<Notification> notificationList){
        return notificationList.stream()
                .map(notification -> NotificationResponseDto.ReadNotificationListResponseDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build())
                .collect(Collectors.toList());
    }
}
