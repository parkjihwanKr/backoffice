package com.example.backoffice.domain.notification.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class NotificationsRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateForAdminDto {
        private String message;
        private List<Long> excludedMemberIdList;
        private List<MemberDepartment> excludedMemberDepartment;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteDto {
        private List<String> notificationIds;
    }
}
