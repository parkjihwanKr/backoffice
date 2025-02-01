package com.example.backoffice.domain.notification.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationsResponseDto {

    // 멤버, 게시글, 댓글, 대댓글 리액션에 대한 알림 요청
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "NotificationsResponseDto.CreateOneDto",
            description = "알림 하나 생성 응답 DTO")
    public static class CreateOneDto {
        private String notificationId;
        private String message;
        private String toMemberName;
        private String fromMemberName;
        private MemberDepartment memberDepartment;
        private MemberDepartment adminRole;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "NotificationsResponseDto.ReadOneDto",
            description = "알림 하나 조회 응답 DTO")
    public static class ReadOneDto {
        private String notificationId;
        private String toMemberName;
        private String fromMemberName;
        private NotificationType notificationType;
        private MemberDepartment fromMemberDepartment;
        private MemberPosition fromMemberPosition;
        private LocalDateTime createdAt;
        private Boolean isRead;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "NotificationsResponseDto.ReadDto",
            description = "알림 조회 응답 DTO")
    public static class ReadDto {
        private String notificationId;
        private String toMemberName;
        private String fromMemberName;
        private String message;
        private LocalDateTime createdAt;
        private Boolean isRead;
        private NotificationType notificationType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "NotificationsResponseDto.ReadSummaryOneDto",
            description = "요약된 알림 하나 조회 응답 DTO")
    public static class ReadSummaryOneDto {
        private String notificationId;
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
        private Boolean isRead;
    }
}
