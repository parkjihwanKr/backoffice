package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j(topic = "CommonAspectImpl")
public class CommonAspectImpl implements CommonAspect {

    private final AuditLogService auditLogService;
    private static final long THRESHOLD_MS = 100;

    @Override
    public String getCurrentMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    @Override
    public Members getLoginMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("loginMember Name : {}", authentication.getName());
        log.info("member authentication status : {}", authentication.isAuthenticated());

        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof MemberDetailsImpl) {
                MemberDetailsImpl memberDetails = (MemberDetailsImpl) principal;
                return memberDetails.getMembers();
            } else {
                log.warn("Principal is not of type MemberDetailsImpl. Found: {}", principal.getClass());
            }
        }
        return null;
    }


    @Override
    public void getLogMessage(String message) {
        log.info(message);
    }

    @Override
    public void auditLogServiceSave(
            AuditLogType auditLogType, String memberName, String message,
            MemberDepartment department, MemberPosition position){
        auditLogService.save(auditLogType, memberName, message, department, position);
    }

    @Around("execution(* com.example.backoffice.domain..repository..*(..))")
    public Object logExecutionRepositoryLogicTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();
        long executionTime = stopWatch.getTotalTimeMillis();

        if (executionTime > THRESHOLD_MS) {
            log.warn("SLOW QUERY [{}] executed in {} ms",
                    joinPoint.getSignature(), executionTime);
        }else{
            log.info("your domain {} : ",joinPoint.getSignature());
            log.info("YOUR QUERY [{}] executed in {} ms",
                    joinPoint.getSignature().getName(), executionTime);
        }

        return result;
    }

}
