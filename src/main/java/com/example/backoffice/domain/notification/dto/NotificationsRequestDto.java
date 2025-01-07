package com.example.backoffice.domain.notification.dto;

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
    public static class CreateByAdminDto {
        private String message;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateFilteredByAdminDto {
        private String message;
        private List<Long> excludedMemberIdList;
        private List<String> excludedMemberDepartment;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteDto {
        private List<String> notificationIds;
    }
}
