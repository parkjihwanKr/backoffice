package com.example.backoffice.global.aop;

import org.aspectj.lang.JoinPoint;

public interface CommonAspect {
    String getCurrentMethodName(JoinPoint joinPoint);
    String getLoginMemberName();
}
