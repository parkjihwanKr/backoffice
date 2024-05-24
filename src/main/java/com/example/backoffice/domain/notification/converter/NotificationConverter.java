package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.admin.entity.Admin;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.notification.dto.NotificationResponseDto;
import com.example.backoffice.domain.notification.entity.Notification;
import com.example.backoffice.domain.notification.entity.NotificationType;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class NotificationConverter {

    public static Notification toEntity(
            String toMemberName, String fromMemberName, String message,
            NotificationType notificationType, MemberRole memberRole){
        return Notification.builder()
                .toMemberName(toMemberName)
                .fromMemberName(fromMemberName)
                .message(message)
                .notificationType(notificationType)
                .memberRole(memberRole)
                .isRead(false)
                .build();
    }

    public static NotificationResponseDto.CreateNotificationResponseDto toCreateOneDto(
            Notification notification){
        return NotificationResponseDto.CreateNotificationResponseDto.builder()
                .toMemberName(notification.getToMemberName())
                .fromMemberName(notification.getFromMemberName())
                .memberRole(notification.getMemberRole())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationResponseDto.ReadNotificationResponseDto toReadOneDto(
            Notification notification){

        return NotificationResponseDto.ReadNotificationResponseDto.builder()
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .memberRole(notification.getMemberRole())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationResponseDto.CreateNotificationListResponseDto toCreateDto(
            Admin mainAdmin, List<MemberRole> memberRoleList, List<Notification> notificationList){
        List<String> fromMemberNameList = new ArrayList<>();
        for (Notification notification : notificationList) {
            fromMemberNameList.add(
                    notification.getFromMemberName());
        }
        return NotificationResponseDto.CreateNotificationListResponseDto.builder()
                .message(notificationList.get(0).getMessage())
                .adminRole(mainAdmin.getRole())
                .fromMemberRoleList(memberRoleList)
                .toMemberName(mainAdmin.getMember().getMemberName())
                .fromMemberNameList(fromMemberNameList)
                .build();
    }

    public static Page<NotificationResponseDto.ReadNotificationListResponseDto> toReadListDto(
            Page<Notification> notificationPage){
        return notificationPage.map(
                notification -> NotificationResponseDto.ReadNotificationListResponseDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .build());
    }
}
