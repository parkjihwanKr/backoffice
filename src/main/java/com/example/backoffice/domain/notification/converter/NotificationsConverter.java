package com.example.backoffice.domain.notification.converter;

import com.example.backoffice.domain.board.entity.Boards;
import com.example.backoffice.domain.comment.entity.Comments;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.entity.Notifications;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationsConverter {

    public static Notifications toEntity(
            String toMemberName, String fromMemberName, String message,
            NotificationType notificationType, MemberDepartment memberDepartment) {
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
            Boards board, Comments comment, Comments reply, Events event,
            String message) {
        return NotificationData.builder()
                .toMember(toMember)
                .fromMember(fromMember)
                .board(board)
                .comment(comment)
                .reply(reply)
                .event(event)
                .message(message)
                .build();
    }

    public static NotificationData toNotificationData(
            Members toMember, Members fromMember, String message) {
        return NotificationData.builder()
                .toMember(toMember)
                .fromMember(fromMember)
                .message(message)
                .build();
    }

    public static NotificationsResponseDto.ReadOneDto toReadOneDto(
            Notifications notification, MemberPosition position) {
        return NotificationsResponseDto.ReadOneDto.builder()
                .notificationId(notification.getId())
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .notificationType(notification.getNotificationType())
                .fromMemberDepartment(notification.getFromMemberDepartment())
                .fromMemberPosition(position)
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .message(notification.getMessage())
                .build();
    }

    public static Page<NotificationsResponseDto.ReadDto> toReadDto(
            Page<Notifications> notificationPage) {
        return notificationPage.map(
                notification -> NotificationsResponseDto.ReadDto.builder()
                        .notificationId(notification.getId())
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .message(notification.getMessage())
                        .notificationType(notification.getNotificationType())
                        .build());
    }

    public static List<NotificationsResponseDto.ReadSummaryOneDto> toReadAllDto(
            List<Notifications> notificationList) {
        return notificationList.stream()
                .map(notification -> NotificationsResponseDto.ReadSummaryOneDto.builder()
                        .notificationId(notification.getId())
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build())
                .collect(Collectors.toList());
    }
}