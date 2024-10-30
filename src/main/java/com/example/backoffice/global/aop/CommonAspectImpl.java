package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommonAspectImpl implements CommonAspect {

    private final AuditLogService auditLogService;

    @Override
    public String getCurrentMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    @Override
    public String getLoginMemberName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymousUser";
    }

    @Override
    public Members getLoginMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Members) {
                return (Members) principal; // Principal을 Members로 캐스팅하여 반환
            }
        }
        return null;
    }

    @Override
    public void getLogMessage(String message) {
        log.info(message);
    }

    @Override
    @Transactional
    public void auditLogServiceSave(
            AuditLogType auditLogType, String memberName, String message,
            MemberDepartment department, MemberPosition position){
        auditLogService.save(auditLogType, memberName, message, department, position);
    }
}
