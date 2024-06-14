package com.example.backoffice.global.aop;

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
public class FileAspect extends CommonAspect {

    private final AuditLogService auditLogService;

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesService.*(..))")
    public void logAfterAllMethod(JoinPoint joinPoint) {
        String methodName = getCurrentMethodName(joinPoint);
        String message = "";
        if (methodName.equals("createFileForMemberRole")
                || methodName.equals("createFileForBoard") || methodName.equals("createImage")) {
            message = getLoginMemberName() + "님이 파일을 생성하셨습니다.";
            auditLogService.saveLogEvent(AuditLogType.CREATE_FILE, getLoginMemberName(), message);
        } else if (methodName.equals("deleteFile") || methodName.equals("deleteImage")) {
            message = getLoginMemberName() + "님이 파일을 삭제하셨습니다.";
            auditLogService.saveLogEvent(AuditLogType.DELETE_FILE, getLoginMemberName(), message);
        } else {
            message = getLoginMemberName() + "님이 알 수 없는 메서드를 불렀습니다.";
            auditLogService.saveLogEvent(AuditLogType.FILE_ERROR, getLoginMemberName(), message);
        }
    }
}
