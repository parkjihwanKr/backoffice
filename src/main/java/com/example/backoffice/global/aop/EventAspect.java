package com.example.backoffice.global.aop;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacade;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class EventAspect extends CommonAspect{

    private final AuditLogService auditLogService;
    private final MembersService membersService;
    private final NotificationsServiceFacade notificationsServiceFacade;

    @AfterThrowing(pointcut = "execution(* com.example.backoffice.domain.event.service(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String errorMessage = createErrorMessage(joinPoint, error);

        // 해당 관련 오류는 없지만 확장의 가능성이 있기에 임시로 만듦.
        if (error instanceof EventsCustomException exception) {
            if (exception.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
                // 실시간 알림 전송
                Members ceo = membersService.findByDepartmentAndPosition(
                        MemberDepartment.HR, MemberPosition.CEO);
                Members itManager = membersService.findByDepartmentAndPosition(
                        MemberDepartment.IT, MemberPosition.MANAGER);
                notificationsServiceFacade.createNotification(
                        NotificationsConverter.toNotificationData(
                                itManager, ceo, null, null, null, null,
                                errorMessage),
                        NotificationType.URGENT_SERVER_ERROR);
            }
        }

        String currentMemberName = getLoginMemberName();
        auditLogService.saveLogEvent(
                AuditLogType.MEMBER_ERROR, currentMemberName, errorMessage);
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
