package com.example.backoffice.global.audit.service;

import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;

import java.util.List;

public interface AuditLogService {

    void save(AuditLogType auditLogType, String username, String details);
    AuditLog readOne(String auditLogId);
    List<AuditLog> readAll();
    AuditLog readMemberLog(String memberName);
    List<AuditLog> readByDepartment(String department);
    List<AuditLog> readByPosition(String position);
}
