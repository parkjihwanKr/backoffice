package com.example.backoffice.domain.notification.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class NotificationsResponseDto {

    // 멤버, 게시글, 댓글, 대댓글 리액션에 대한 알림 요청
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto {
        private String notificationId;
        private String message;
        private String toMemberName;
        private String fromMemberName;
        private MemberDepartment memberDepartment;
        private MemberDepartment adminRole;
        private LocalDateTime createdAt;
    }

    // 어드민의 전체 알림 발송
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateForAdminDto {
        private List<String> notificationIdList;
        private String message;
        private String fromMemberName;
        private List<String> toMemberNameList;
        private Set<MemberDepartment> toMemberDepartmentSet;
        private MemberRole fromAdminRole;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto {
        private String notificationId;
        private String toMemberName;
        private String fromMemberName;
        private MemberDepartment fromMemberDepartment;
        private LocalDateTime createdAt;
        private Boolean isRead;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadDto {
        private String notificationId;
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
        private Boolean isRead;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadAllDto {
        private String notificationId;
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
        private Boolean isRead;
    }
}
