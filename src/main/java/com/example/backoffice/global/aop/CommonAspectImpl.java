package com.example.backoffice.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Aspect
public class CommonAspectImpl implements CommonAspect{

    @Pointcut("execution(* com.example.backoffice.domain.file.service.FilesService.*(..))" +
            " || execution(* com.example.backoffice.domain.member.service.MembersService.*(..))" +
            " || execution(* com.example.backoffice.domain.event.facade.EventsServiceFacadeV1.*(..))")
    public void commonPointcut() {}

    @Before("commonPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("실행 중 : " + getCurrentMethodName(joinPoint));
    }

    @AfterReturning(pointcut = "commonPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("실행 후 : " + getCurrentMethodName(joinPoint) + ", 결과 : " + result);
    }

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
}
