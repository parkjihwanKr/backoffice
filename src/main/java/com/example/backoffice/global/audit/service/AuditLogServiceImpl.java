package com.example.backoffice.global.audit.service;

import com.example.backoffice.global.audit.converter.AuditLogConverter;
import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public void saveLogEvent(
            AuditLogType auditLogType, String username, String details){
        AuditLog auditLog
                = AuditLogConverter.toEntity(auditLogType, username, details);
        auditLogRepository.save(auditLog);
    }
}
