package com.example.backoffice.global.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class CommonAspect {
    public String getCurrentMethodName(JoinPoint joinPoint){
        return joinPoint.getSignature().getName();
    }

    public String getLoginMemberName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymousUser";
    }
}
