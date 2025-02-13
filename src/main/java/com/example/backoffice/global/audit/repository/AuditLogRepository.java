package com.example.backoffice.global.audit.repository;

import com.example.backoffice.global.audit.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuditLogRepository extends MongoRepository<AuditLog, String>, AuditLogCustomRepository {

    Optional<AuditLog> findByMemberName(String memberName);
}
