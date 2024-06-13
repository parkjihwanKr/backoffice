package com.example.backoffice.global.aop;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.member.entity.Members;
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
public class EventAspect extends CommonAspect{

    private final AuditLogService auditLogService;

    @Before("execution(* com.example.backoffice.domain.event.service.*(..))")
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

    // 개인 휴가에 대한 이벤트만 고려
    /*@Param
    Members loginMember, EventsRequestDto.CreateVacationRequestDto requestDto*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.event.service.EventsServiceImplV1.createVacationEvent(..))")
    public void logAfterCreateVacationEvent(JoinPoint joinPoint){
        Members member = (Members) joinPoint.getArgs()[0];
        EventsRequestDto.CreateVacationRequestDto requestDto
                = (EventsRequestDto.CreateVacationRequestDto)joinPoint.getArgs()[1];
        String message = "제목 : "+ requestDto.getTitle()
                + " / 이유 : "+ requestDto.getReason();
        if(requestDto.getUrgent()){
            message = "제목 : 긴급한 휴가 요청 / 이유 : "+ requestDto.getReason();
        }
        auditLogService.saveLogEvent(
                AuditLogType.CREATE_MEMBER_VACATION, member.getMemberName(), message);
    }

    /*@Param Long vacationId, Members loginMember,
            EventsRequestDto.UpdateVacationEventRequestDto requestDto*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.event.service.EventsServiceImplV1.updateVacationEvent(..))")
    public void logAfterUpdateVacationEvent(JoinPoint joinPoint){
        Members member = (Members) joinPoint.getArgs()[1];
        EventsRequestDto.CreateVacationRequestDto requestDto
                = (EventsRequestDto.CreateVacationRequestDto)joinPoint.getArgs()[2];

        String message = "제목 : "+ requestDto.getTitle()
                + " / 이유 : "+ requestDto.getReason();
        if(requestDto.getUrgent()){
            message = "제목 : 긴급한 휴가 요청 / 이유 : "+ requestDto.getReason();
        }
        auditLogService.saveLogEvent(
                AuditLogType.UPDATE_MEMBER_VACATION, member.getMemberName(), message);
    }
}
