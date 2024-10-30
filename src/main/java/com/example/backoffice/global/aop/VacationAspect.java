package com.example.backoffice.global.aop;

import com.example.backoffice.domain.expense.exception.ExpenseCustomException;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.global.audit.entity.AuditLogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
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
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1.updateOne(..))")
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
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1.updateOneForAdmin(..))", returning = "result")
    public void logAfterUpdateVacationForAdmin(JoinPoint joinPoint, VacationsResponseDto.UpdateOneForAdminDto result) {
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

    @AfterThrowing(pointcut = "vacationServiceFacadeMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, ExpenseCustomException ex) {
        Members member = commonAspect.getLoginMemberInfo();
        String methodName = commonAspect.getCurrentMethodName(joinPoint); // 메서드 이름 가져오기

        if(member == null){
            String anonymousUser = "anonymousUser";
            String message = "Error in method: " + methodName + " | "
                    + anonymousUser+" 님이 "
                    + "휴가 처리 도중 에러가 발생했습니다. "
                    + "Exception: " + ex.getMessage()
                    + "추가적인 조치가 필요합니다.";

            commonAspect.getLogMessage(message);
            commonAspect.auditLogServiceSave(AuditLogType.VACATION_ERROR, anonymousUser, message,
                    null, null);
            return;
        }
        String message = "Error in method: " + methodName + " | "
                + member.getMemberName() + "님이 "
                + "지출 내역 처리 중 오류가 발생했습니다. "
                + "Exception: " + ex.getMessage();

        commonAspect.getLogMessage(message);
        commonAspect.auditLogServiceSave(AuditLogType.VACATION_ERROR, member.getMemberName(), message,
                member.getDepartment(), member.getPosition());
    }
}
