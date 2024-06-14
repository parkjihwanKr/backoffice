package com.example.backoffice.global.aop;

import org.aspectj.lang.JoinPoint;

public interface CommonAspect {
    void logBefore(JoinPoint joinPoint);
    void logAfterReturning(JoinPoint joinPoint, Object result);
    String getCurrentMethodName(JoinPoint joinPoint);
    String getLoginMemberName();
    String createErrorMessage(JoinPoint joinPoint, Throwable error);
}
