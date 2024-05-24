package com.example.backoffice.domain.notification.dto;

import com.example.backoffice.domain.admin.entity.AdminRole;
import com.example.backoffice.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDto {

    // 멤버, 게시글, 댓글, 대댓글 리액션에 대한 알림 요청
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateNotificationResponseDto {
        private String message;
        private String toMemberName;
        private String fromMemberName;
        private MemberRole memberRole;
        private AdminRole adminRole;
        private LocalDateTime createdAt;
    }

    // 어드민의 전체 알림 발송
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateNotificationListResponseDto {
        private String message;
        private String toMemberName;
        private List<String> fromMemberNameList;
        private List<MemberRole> fromMemberRoleList;
        private AdminRole adminRole;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadNotificationResponseDto {
        private String toMemberName;
        private String fromMemberName;
        private MemberRole memberRole;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadNotificationListResponseDto {
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
    }
}
