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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto {
        private String toMemberName;
        private String fromMemberName;
        private MemberDepartment fromMemberDepartment;
        private LocalDateTime createdAt;
        private Boolean isRead;
        private String message;
    }

    // 어드민의 전체 알림 발송
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForAdminDto {
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
    public static class ReadAllDto {
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
        private Boolean isRead;
    }
}
