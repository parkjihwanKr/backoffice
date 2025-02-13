package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.global.audit.entity.AuditLogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class VacationAspect {

    private final CommonAspect commonAspect;

    @Pointcut("execution(* com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1.*(..))")
    public void vacationServiceFacadeMethods() {}

    /*Long vacationId, Members loginMember, VacationsRequestDto.UpdateOneDto requestDto*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1.updateOneByMember(..))")
    public void logAfterUpdateVacation(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        VacationsRequestDto.UpdateOneDto requestDto
                = (VacationsRequestDto.UpdateOneDto) joinPoint.getArgs()[2];
        String message = loginMember.getMemberName()
                + "님이 '"
                + requestDto.getUrgentReason()
                +"' 이유로 휴가를 수정했습니다.";

        commonAspect.getLogMessage(message);

        commonAspect.auditLogServiceSave(
                AuditLogType.UPDATE_MEMBER_VACATION, loginMember.getMemberName(), message,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    /*Long vacationId, Members loginMember*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1.updateOneByAdmin(..))", returning = "result")
    public void logAfterUpdateVacationForAdmin(JoinPoint joinPoint, VacationsResponseDto.UpdateOneByAdminDto result) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName()
                + "님이 '"
                + result.getAcceptedVacationMemberName()
                + "님의 휴가 요청을 변경하였습니다.";

        commonAspect.getLogMessage(message);

        commonAspect.auditLogServiceSave(
                AuditLogType.UPDATE_MEMBER_VACATION, loginMember.getMemberName(), message,
                loginMember.getDepartment(), loginMember.getPosition());
    }
}
