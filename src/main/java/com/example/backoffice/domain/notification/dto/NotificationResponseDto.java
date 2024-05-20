package com.example.backoffice.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateNotificationResponseDto {
        private String message;
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadNotificationResponseDto {
        private String toMemberName;
        private String fromMemberName;
        private LocalDateTime createdAt;
    }
}
