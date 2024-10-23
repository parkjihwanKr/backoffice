package com.example.backoffice.global.audit.repository;

import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class AuditLogCustomRepositoryImpl implements AuditLogCustomRepository {

    private final MongoTemplate mongoTemplate;

    public AuditLogCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<AuditLog> findFilteredAuditLogs(
            String memberName, AuditLogType auditType,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {

        Query query = new Query();

        if (memberName != null) {
            query.addCriteria(Criteria.where("member_name").is(memberName));
        }
        if (auditType != null) {
            query.addCriteria(Criteria.where("audit_log_type").is(auditType));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("created_at").gte(startDate).lte(endDate));
        }

        // Pageable 처리
        long count = mongoTemplate.count(query, AuditLog.class);
        List<AuditLog> auditLogs = mongoTemplate.find(query.with(pageable), AuditLog.class);

        return PageableExecutionUtils.getPage(auditLogs, pageable, () -> count);
    }
}
