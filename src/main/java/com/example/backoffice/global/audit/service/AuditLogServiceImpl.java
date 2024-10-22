package com.example.backoffice.global.audit.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.global.audit.converter.AuditLogConverter;
import com.example.backoffice.global.audit.dto.AuditLogResponseDto;
import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.repository.AuditLogRepository;
import com.example.backoffice.global.date.DateTimeUtils;
import com.example.backoffice.global.exception.AuditLogCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final MembersServiceV1 membersService;

    @Override
    @Transactional
    public void save(
            AuditLogType auditLogType, String username, String details){
        AuditLog auditLog
                = AuditLogConverter.toEntity(auditLogType, username, details);
        auditLogRepository.save(auditLog);
    }

    // 상세보기
    @Override
    @Transactional(readOnly = true)
    public AuditLog readOne(String auditLogId){
        return auditLogRepository.findById(auditLogId).orElseThrow(
                ()-> new AuditLogCustomException(GlobalExceptionCode.NOT_FOUND_AUDIT_LOG));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponseDto.ReadOneDto> readFiltered(
            Members loginMember, String memberName, String auditType,
            String startDate, String endDate, Pageable pageable){
        // 1. 로그인 멤버가 해당 로그를 볼 수 있는 권한인지?
        membersService.findAuditManagerOrCeo(loginMember.getId());

        // 2. 해당 멤버가 존재하는 사람인지?
        membersService.findByMemberName(memberName);

        // 3. 해당하는 조건에 맞춰진 AuditLog Page 구성
        AuditLogType auditLogType = (auditType != null) ? AuditLogConverter.toAuditType(auditType) : null;
        LocalDateTime customStartDate = (startDate != null) ? DateTimeUtils.parse(startDate) : null;
        LocalDateTime customEndDate = (endDate != null) ? DateTimeUtils.parse(endDate) : null;

        Page<AuditLog> auditLogPage
                = auditLogRepository.findFilteredAuditLogs(
                        memberName, auditLogType, customStartDate, customEndDate, pageable);

        return AuditLogConverter.toAuditLogPageDto(auditLogPage);
    }
}