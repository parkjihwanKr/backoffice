package com.example.backoffice.global.audit.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.dto.AuditLogResponseDto;
import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {

    void save(AuditLogType auditLogType, String username, String details,
              MemberDepartment department, MemberPosition position);

    AuditLog readOne(String auditLogId);

    Page<AuditLogResponseDto.ReadOneDto> readFiltered(
            Members loginMember, String memberName, String AuditType,
            String startDate, String endDate, Pageable pageable);
}
