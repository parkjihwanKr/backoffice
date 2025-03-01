package com.example.backoffice.global.audit.repository;

import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AuditLogCustomRepository {
    Page<AuditLog> findFiltered(
            String memberName, AuditLogType auditType,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
