package com.example.backoffice.global.audit.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import com.example.backoffice.global.audit.converter.AuditLogConverter;
import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.repository.AuditLogRepository;
import com.example.backoffice.global.exception.AuditLogCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final MembersServiceFacadeV1 membersServiceFacade;

    @Override
    @Transactional
    public void save(
            AuditLogType auditLogType, String username, String details){
        AuditLog auditLog
                = AuditLogConverter.toEntity(auditLogType, username, details);
        auditLogRepository.save(auditLog);
    }

    // 근데 해당 부분을 관리자에 대한 검증을 해야할까? -> 이거에 대해서 한 번 생각해보기
    // Controller에서 받는 url이 없음
    // 즉, 해당 부분은 감사 관리자 또는 메인 관리자에 의한 접근만 가능해야함
    // 이걸 service 비지니스 로직에서 권한에 대한 확인을 해야하는게 분명한데
    // 따른 곳에서 권한에 대한 확인을 할 수 있는지 확인 -> 이에 대한 타당성을 명확히 할 것.

    @Override
    @Transactional(readOnly = true)
    public AuditLog readOne(String auditLogId){
        return auditLogRepository.findById(auditLogId).orElseThrow(
                ()-> new AuditLogCustomException(GlobalExceptionCode.NOT_FOUND_AUDIT_LOG));
    }

    // 특정인 조회
    @Override
    @Transactional(readOnly = true)
    public AuditLog readMemberLog(String memberName){
        return findByMemberName(memberName);
    }

    // 부서에 따른 조회
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> readByDepartment(String department){
        List<Members> memberList = membersServiceFacade.findAllByDepartment(department);
        List<AuditLog> auditLogList = new ArrayList<>();
        for(Members member : memberList){
            AuditLog auditLog = findByMemberName(member.getMemberName());
            auditLogList.add(auditLog);
        }
        return auditLogList;
    }

    // 직위에 따른 조회
    @Override
    @Transactional
    public List<AuditLog> readByPosition(String position){
        List<Members> memberList = membersServiceFacade.findAllByPosition(position);
        List<AuditLog> auditLogList = new ArrayList<>();
        for(Members member : memberList){
            AuditLog auditLog = findByMemberName(member.getMemberName());
            auditLogList.add(auditLog);
        }
        return auditLogList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> readAll(){
        return auditLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AuditLog findByMemberName(String memberName){
        return auditLogRepository.findByMemberName(memberName).orElseThrow(
                ()-> new AuditLogCustomException(GlobalExceptionCode.NOT_FOUND_AUDIT_LOG));
    }
}