package com.example.backoffice.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "NotificationsRequestDto.CreateByAdminDto",
            description = "관리자에 의해 만들어질 알림 요청 DTO")
    public static class CreateByAdminDto {
        private String message;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "NotificationsRequestDto.CreateByAdminDto",
            description = "관리자에 의해 특정 인원들에게 보내지는 알림 요청 DTO")
    public static class CreateFilteredByAdminDto {
        private String message;
        private List<Long> excludedMemberIdList;
        private List<String> excludedMemberDepartment;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "NotificationsRequestDto.DeleteDto",
            description = "삭제할 알림 아이디 리스트 요청 DTO")
    public static class DeleteDto {
        private List<String> notificationIds;
    }
}
