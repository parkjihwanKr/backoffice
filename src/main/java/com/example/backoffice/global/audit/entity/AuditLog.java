package com.example.backoffice.global.audit.entity;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLog{

    @Id
    private String id;

    @Field("audit_log_type")
    private AuditLogType auditLogType;

    @Field("member_name")
    private String memberName;

    private String details;

    @Field("department")
    private MemberDepartment department;

    @Field("position")
    private MemberPosition position;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("modified_at")
    private LocalDateTime modifiedAt;
}
