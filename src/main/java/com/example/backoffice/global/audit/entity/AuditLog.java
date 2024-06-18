package com.example.backoffice.global.audit.entity;

import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "audit_logs")
public class AuditLog extends CommonEntity {

    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private AuditLogType auditLogType;
    private String memberName;
    private String details;
}
