package com.example.backoffice.global.aop;

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
public class FileAspect {

    private final AuditLogService auditLogService;

    @Before("execution(* com.example.backoffice.domain.file.service.FilesService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Executing: " + joinPoint.getSignature().toShortString());
        //auditLogService.saveLogEvent(null, null, null);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesService.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Executed: " + joinPoint.getSignature().toShortString() + ", Returned: " + result);
        //auditLogService.saveLogEvent(null, null, null);
    }

    @AfterThrowing(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesService.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String errorMessage
                = "Exception in: " + joinPoint.getSignature().toShortString() +" "+ error;
        log.error("Exception in: " + joinPoint.getSignature().toShortString(), error);
        auditLogService.saveLogEvent(null, null, errorMessage);
    }
}
