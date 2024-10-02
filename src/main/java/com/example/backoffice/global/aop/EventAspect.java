package com.example.backoffice.global.aop;

import com.example.backoffice.global.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EventAspect {

    private final AuditLogService auditLogService;
    private final CommonAspectImpl commonAspect;
    /*// 개인 휴가에 대한 이벤트만 고려
    *//*@Param
    Members loginMember, EventsRequestDto.CreateVacationRequestDto requestDto*//*
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.event.facade.EventsServiceFacadeV1.createOneForVacationEvent(..))")
    public void logAfterCreateVacationEvent(JoinPoint joinPoint){
        Members member = (Members) joinPoint.getArgs()[0];
        EventsRequestDto.CreateOneForVacationEventDto requestDto
                = (EventsRequestDto.CreateOneForVacationEventDto)joinPoint.getArgs()[1];
        String message = "제목 : "+ requestDto.getTitle()
                + " / 이유 : "+ requestDto.getReason();
        if(requestDto.getUrgent()){
            message = "제목 : 긴급한 휴가 요청 / 이유 : "+ requestDto.getReason();
        }
        commonAspect.getLogMessage(message);
        auditLogService.save(
                AuditLogType.CREATE_MEMBER_VACATION, member.getName(), message);
    }

    *//*@Param Long vacationId, Members loginMember,
            EventsRequestDto.UpdateVacationEventRequestDto requestDto*//*
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.event.facade.EventsServiceFacadeV1.updateOneForVacationEvent(..))")
    public void logAfterUpdateVacationEvent(JoinPoint joinPoint){
        Members member = (Members) joinPoint.getArgs()[1];
        EventsRequestDto.UpdateOneForVacationEventDto requestDto
                = (EventsRequestDto.UpdateOneForVacationEventDto)joinPoint.getArgs()[2];

        String message
                = "제목 : " + member.getName() + "님의 휴가 수정"
                + " / 이유 : "+ requestDto.getReason();
        if(requestDto.getUrgent()){
            message = "제목 : 긴급한 휴가 요청 / 이유 : "+ requestDto.getReason();
        }
        commonAspect.getLogMessage(message);
        auditLogService.save(
                AuditLogType.UPDATE_MEMBER_VACATION, member.getName(), message);
    }*/
}
