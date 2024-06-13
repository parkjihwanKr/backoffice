package com.example.backoffice.global.aop;

import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FileAspect extends CommonAspect{

    private final AuditLogService auditLogService;

    @Before("execution(* com.example.backoffice.domain.file.service.FilesService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("실행 중 : " + getCurrentMethodName(joinPoint));
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesService.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("실행 후 : " + getCurrentMethodName(joinPoint) + ", 결과 : " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesService.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String errorMessage
                = "예외 : " + getCurrentMethodName(joinPoint) +" "+ error;
        log.error(errorMessage, error);
        auditLogService.saveLogEvent(AuditLogType.FILE_ERROR, getLoginMemberName(), errorMessage);
    }

}
