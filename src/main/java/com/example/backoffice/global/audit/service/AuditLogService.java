package com.example.backoffice.global.audit.service;

import com.example.backoffice.global.audit.entity.AuditLogType;

public interface AuditLogService {

    void saveLogEvent(
            AuditLogType auditLogType, String username, String details);
}
