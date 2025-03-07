package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.entity.AuditLogType;
import org.aspectj.lang.JoinPoint;

public interface CommonAspect {
    String getCurrentMethodName(JoinPoint joinPoint);

    Members getLoginMemberInfo();

    void getLogMessage(String message);

    void auditLogServiceSave(
            AuditLogType auditLogType, String memberName, String message,
            MemberDepartment department, MemberPosition position);
}
