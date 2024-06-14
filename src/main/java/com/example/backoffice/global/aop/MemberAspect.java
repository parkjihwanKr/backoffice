package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
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
public class MemberAspect extends CommonAspect {

    private final AuditLogService auditLogService;
    /*private final MembersService membersService;
    private final NotificationsServiceFacade notificationsServiceFacade;*/

    /*@AfterThrowing(pointcut = "execution(* com.example.backoffice.domain.member.facade.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String errorMessage = createErrorMessage(joinPoint, error);

        if (error instanceof MembersCustomException exception) {
            if (exception.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
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
    }*/

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.signup(..))")
    public void logAfterSignup(JoinPoint joinPoint) {
        MembersRequestDto.CreateMembersRequestDto requestDto
                = (MembersRequestDto.CreateMembersRequestDto) joinPoint.getArgs()[0];
        String message = requestDto.getMemberName() + "님이 회원가입을 진행하셨습니다.";
        log.info(message);
        auditLogService.saveLogEvent(
                AuditLogType.SIGNUP, requestDto.getMemberName(), message);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.updateSalary(..))")
    public void logAfterUpdateSalary(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        MembersRequestDto.UpdateMemberSalaryRequestDto requestDto =
                (MembersRequestDto.UpdateMemberSalaryRequestDto) joinPoint.getArgs()[2];
        String message
                = loginMember.getMemberName() + "님이 "
                + requestDto.getMemberName() + "님의 급여를 "
                + requestDto.getSalary() + "로 변경하셨습니다.";

        auditLogService.saveLogEvent(
                AuditLogType.CHANGE_MEMBER_SALARY,
                loginMember.getMemberName(), message);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.deleteMember(..))")
    public void logAfterDeleteMember(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName() + "님이 회원 탈퇴하셨습니다.";

        auditLogService.saveLogEvent(
                AuditLogType.DELETE_MEMBER,
                loginMember.getMemberName(), message);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.updateAttribute(..))")
    public void logAfterUpdateMemberAttribute(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        MembersRequestDto.UpdateMemberAttributeRequestDto requestDto =
                (MembersRequestDto.UpdateMemberAttributeRequestDto) joinPoint.getArgs()[2];

        if (requestDto.getSalary() != null
                && requestDto.getPosition() != null && requestDto.getDepartment() != null) {
            String message = loginMember.getMemberName()
                    + "님이 "
                    + requestDto.getMemberName()
                    + "님의 급여, 부서, 직책을 "
                    + requestDto.getSalary() + ", "
                    + requestDto.getDepartment() + ", "
                    + requestDto.getPosition()
                    + "로 변경하였습니다.";

            auditLogService.saveLogEvent(
                    AuditLogType.CHANGE_MEMBER_ATTRIBUTE,
                    loginMember.getMemberName(), message);
        }
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.updateProfileImageUrl(..))")
    public void logAfterUpdateMemberProfileImageUrl(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName()
                + "님이 파일을 업로드 하셨습니다.";
        auditLogService.saveLogEvent(
                AuditLogType.UPLOAD_MEMBER_FILE,
                loginMember.getMemberName(), message);
    }
}
