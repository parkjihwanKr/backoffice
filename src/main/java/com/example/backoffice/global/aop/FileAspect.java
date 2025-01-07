package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FileAspect {

    private final CommonAspect commonAspect;

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesServiceV1.*(..))")
    public void logAfterAllMethod(JoinPoint joinPoint) {
        String methodName = commonAspect.getCurrentMethodName(joinPoint);
        String message = "";
        Members loginMember = commonAspect.getLoginMemberInfo();
        if(loginMember == null){
            log.error("해당 로그인 멤버에 대한 인증 요청이 이루어지지 않았습니다.");
        }
        if (methodName.equals("createOneForMemberRole")
                || methodName.equals("createOneForBoard") || methodName.equals("createMemberProfileImage")
                || methodName.equals("createOneForEvent") || methodName.equals("createOneForExpense")) {
            message = loginMember.getMemberName() + "님이 파일을 생성하셨습니다.";
            commonAspect.auditLogServiceSave(
                    AuditLogType.CREATE_FILE, loginMember.getMemberName(), message,
                    loginMember.getDepartment(), loginMember.getPosition());

        } else if (methodName.equals("deleteForBoard") || methodName.equals("deleteImage")
                || methodName.equals("deleteForEvent") || methodName.equals("deleteForExpense")) {
            message = loginMember.getMemberName() + "님이 파일을 삭제하셨습니다.";
            commonAspect.auditLogServiceSave(
                    AuditLogType.DELETE_FILE, loginMember.getMemberName(), message,
                    loginMember.getDepartment(), loginMember.getPosition());
        } else {
            message = loginMember.getMemberName() + "님이 알 수 없는 메서드를 불렀습니다.";
            commonAspect.auditLogServiceSave(
                    AuditLogType.FILE_ERROR, loginMember.getMemberName(), message,
                    loginMember.getDepartment(), loginMember.getPosition());
        }
        commonAspect.getLogMessage(message);
    }
}
