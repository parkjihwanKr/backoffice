package com.example.backoffice.global.audit.repository;

import com.example.backoffice.global.audit.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String>, AuditLogCustomRepository {

}
