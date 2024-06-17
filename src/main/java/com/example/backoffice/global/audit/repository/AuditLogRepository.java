package com.example.backoffice.global.audit.repository;

import com.example.backoffice.global.audit.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    Optional<AuditLog> findByMemberName(String memberName);
    List<AuditLog> findAllByMemberName(String memberName);
}
