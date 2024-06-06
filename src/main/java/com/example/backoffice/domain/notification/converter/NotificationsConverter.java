package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationData;
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

    public static NotificationData toNotificationData(
            Members toMember, Members fromMember,
            Boards board, Comments comment, Comments reply, Events event){
        return NotificationData.builder()
                .toMember(toMember)
                .fromMember(fromMember)
                .board(board)
                .comment(comment)
                .reply(reply)
                .event(event)
                .build();
    }

    public static NotificationsResponseDto.CreateNotificationResponseDto toCreateOneDto(
            Notifications notification){
        return NotificationsResponseDto.CreateNotificationResponseDto.builder()
                .toMemberName(notification.getToMemberName())
                .fromMemberName(notification.getFromMemberName())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationsResponseDto.ReadNotificationResponseDto toReadOneDto(
            Notifications notification){

        return NotificationsResponseDto.ReadNotificationResponseDto.builder()
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .fromMemberDepartment(notification.getFromMemberDepartment())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .message(notification.getMessage())
                .build();
    }

    public static NotificationsResponseDto.CreateNotificationListResponseDto toCreateDto(
            Members mainAdmin, Set<MemberDepartment> memberDepartmentSet,
            List<Notifications> notificationList, String message){
        List<String> toMemberNameList = new ArrayList<>();
        for (Notifications notification : notificationList) {
            toMemberNameList.add(
                    notification.getToMemberName());
        }
        return NotificationsResponseDto.CreateNotificationListResponseDto.builder()
                .message(message)
                .fromAdminRole(mainAdmin.getRole())
                .fromMemberName(mainAdmin.getMemberName())
                .toMemberDepartmentSet(memberDepartmentSet)
                .toMemberNameList(toMemberNameList)
                .build();
    }

    public static Page<NotificationsResponseDto.ReadNotificationListResponseDto> toReadListDto(
            Page<Notifications> notificationPage){
        return notificationPage.map(
                notification -> NotificationsResponseDto.ReadNotificationListResponseDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build());
    }

    public static List<NotificationsResponseDto.ReadNotificationListResponseDto> toReadAllDto(
            List<Notifications> notificationList){
        return notificationList.stream()
                .map(notification -> NotificationsResponseDto.ReadNotificationListResponseDto.builder()
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build())
                .collect(Collectors.toList());
    }
}
