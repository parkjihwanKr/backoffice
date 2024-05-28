package com.example.backoffice.domain.notification.dto;

import com.example.backoffice.domain.admin.entity.AdminRole;
import com.example.backoffice.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
        private String fromMemberName;
        private List<String> toMemberNameList;
        private Set<MemberRole> toMemberRoleList;
        private AdminRole fromAdminRole;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadNotificationResponseDto {
        private String toMemberName;
        private String fromMemberName;
        private MemberRole toMemberRole;
        private LocalDateTime createdAt;
        private Boolean isRead;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadNotificationListResponseDto {
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
        private Boolean isRead;
    }
}
