package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.Members;
import org.aspectj.lang.JoinPoint;

public interface CommonAspect {
    String getCurrentMethodName(JoinPoint joinPoint);

    Members getLoginMemberInfo();

    String getLoginMemberName();

    void getLogMessage(String message);
}
