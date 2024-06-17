package com.example.backoffice.global.audit.converter;

import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;

public class AuditLogConverter {

    public static AuditLog toEntity(
            AuditLogType auditLogType, String username, String details) {
        return AuditLog.builder()
                .auditLogType(auditLogType)
                .username(username)
                .details(details)
                .build();
    }
}
