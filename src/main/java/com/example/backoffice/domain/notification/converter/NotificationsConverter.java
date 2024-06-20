package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationsConverter {

    public static Notifications toEntity(
            String toMemberName, String fromMemberName, String message,
            NotificationType notificationType, MemberDepartment memberDepartment){
        return Notifications.builder()
                .toMemberName(toMemberName)
                .fromMemberName(fromMemberName)
                .message(message)
                .notificationType(notificationType)
                .fromMemberDepartment(memberDepartment)
                .isRead(false)
                .build();
    }

    public static NotificationsResponseDto.ReadOneDto toReadOneDto(
            Notifications notification){

        return NotificationsResponseDto.ReadOneDto.builder()
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .fromMemberDepartment(notification.getFromMemberDepartment())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .message(notification.getMessage())
                .build();
    }

    public static NotificationsResponseDto.CreateOneForAdminDto toCreateOneForAdminDto(
            Members mainAdmin, Set<MemberDepartment> memberDepartmentSet,
            List<Notifications> notificationList, String message){
        List<String> toMemberNameList = new ArrayList<>();
        for (Notifications notification : notificationList) {
            toMemberNameList.add(
                    notification.getToMemberName());
        }
        return NotificationsResponseDto.CreateOneForAdminDto.builder()
                .message(message)
                .fromAdminRole(mainAdmin.getRole())
                .fromMemberName(mainAdmin.getMemberName())
                .toMemberDepartmentSet(memberDepartmentSet)
                .toMemberNameList(toMemberNameList)
                .build();
    }

    public static Page<NotificationsResponseDto.ReadAllDto> toReadPageDto(
            Page<Notifications> notificationPage){
        return notificationPage.map(
                notification -> NotificationsResponseDto.ReadAllDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build());
    }

    public static List<NotificationsResponseDto.ReadAllDto> toReadAllDto(
            List<Notifications> notificationList){
        return notificationList.stream()
                .map(notification -> NotificationsResponseDto.ReadAllDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build())
                .collect(Collectors.toList());
    }
}
