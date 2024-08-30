package com.example.backoffice.global.aop;

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
public class LogoutAspect {

    // AOP test
    @Before("execution(* com.example.backoffice.global.security.CustomLogoutHandler.logout(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {}", joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("Argument: {}", arg);
        }
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.global.security.CustomLogoutHandler.logout(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        log.info("Method {} executed successfully", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.example.backoffice.global.security.CustomLogoutHandler.logout(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("An exception has been thrown in {}: {}", joinPoint.getSignature().getName(), error.getMessage());
    }
}
