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

    public static NotificationsResponseDto.ReadOneDto toReadOneDto(
            Notifications notification) {
        return NotificationsResponseDto.ReadOneDto.builder()
                .notificationId(notification.getId())
                .fromMemberName(notification.getFromMemberName())
                .toMemberName(notification.getToMemberName())
                .fromMemberDepartment(notification.getFromMemberDepartment())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .message(notification.getMessage())
                .build();
    }

    /*public static NotificationsResponseDto.CreateForAdminDto toCreateForAdminDto(
            Members mainAdmin, Set<MemberDepartment> memberDepartmentSet,
            List<Notifications> notificationList, String message) {
        List<String> toMemberNameList = new ArrayList<>();
        List<String> notificationIdList = new ArrayList<>();
        for (Notifications notification : notificationList) {
            toMemberNameList.add(
                    notification.getToMemberName());
            notificationIdList.add(notification.getId());
        }

        return NotificationsResponseDto.CreateForAdminDto.builder()
                .notificationIdList(notificationIdList)
                .message(message)
                .fromAdminRole(mainAdmin.getRole())
                .fromMemberName(mainAdmin.getMemberName())
                .toMemberDepartmentSet(memberDepartmentSet)
                .toMemberNameList(toMemberNameList)
                .build();
    }*/

    public static Page<NotificationsResponseDto.ReadDto> toReadDto(
            Page<Notifications> notificationPage) {
        return notificationPage.map(
                notification -> NotificationsResponseDto.ReadDto.builder()
                        .notificationId(notification.getId())
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build());
    }

    public static List<NotificationsResponseDto.ReadAllDto> toReadAllDto(
            List<Notifications> notificationList) {
        return notificationList.stream()
                .map(notification -> NotificationsResponseDto.ReadAllDto.builder()
                        .notificationId(notification.getId())
                        .toMemberName(notification.getToMemberName())
                        .fromMemberName(notification.getFromMemberName())
                        .createdAt(notification.getCreatedAt())
                        .isRead(notification.getIsRead())
                        .build())
                .collect(Collectors.toList());
    }
}