package com.example.backoffice.global.audit.dto;

import com.example.backoffice.global.audit.entity.AuditLogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AuditLogResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneDto {
        private String auditLogId;
        private String details;
        private String memberName;
        private LocalDateTime createdAt;
        private AuditLogType auditLogType;
    }
}
