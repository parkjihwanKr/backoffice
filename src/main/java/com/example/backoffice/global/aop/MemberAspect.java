package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MemberAspect {

    private final AuditLogService auditLogService;
    private final CommonAspect commonAspect;
    /* @PARAM
    JoinPoint -> 각 메서드들의 파라미터들을 가져옴
    joinPoint.getArg[0] -> 첫 번째 파라미터 ...
    jointPoint.getArg[n] -> n-1 번째 파라미터

    @Before : 메서드 실행 전
    @After : 메서드 실행 후
    @AfterReturning : 메서드가 정상적으로 종료된 후
    @AfterThrowing : 메서드가 예외 처리가 된 후
    */

    // JoinPoint @Param MembersRequestDto.CreateMembersRequestDto requestDto
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImplV1.createOneForSignup(..))")
    public void logAfterSignup(JoinPoint joinPoint) {
        MembersRequestDto.CreateOneDto requestDto
                = (MembersRequestDto.CreateOneDto) joinPoint.getArgs()[0];
        String message = requestDto.getMemberName() + "님이 회원가입을 진행하셨습니다.";

        commonAspect.getLogMessage(message);

        commonAspect.auditLogServiceSave(
                AuditLogType.SIGNUP, requestDto.getMemberName(), message,
                MemberDepartment.HR, MemberPosition.INTERN);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImplV1.updateOneForSalaryByAdmin(..))")
    public void logAfterUpdateSalary(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        MembersRequestDto.UpdateOneForSalaryDto requestDto =
                (MembersRequestDto.UpdateOneForSalaryDto) joinPoint.getArgs()[2];
        String message
                = loginMember.getMemberName() + "님이 "
                + requestDto.getMemberName() + "님의 급여를 "
                + requestDto.getSalary() + "로 변경하셨습니다.";

        commonAspect.getLogMessage(message);

        commonAspect.auditLogServiceSave(
                AuditLogType.CHANGE_MEMBER_SALARY, loginMember.getMemberName(), message,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImplV1.deleteOneByAdmin(..))")
    public void logAfterDeleteMember(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName() + "님이 회원 탈퇴하셨습니다.";

        commonAspect.getLogMessage(message);

        auditLogService.save(
                AuditLogType.DELETE_MEMBER, loginMember.getMemberName(), message,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImplV1.updateOneForAttributeByAdmin(..))")
    public void logAfterUpdateMemberAttribute(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];

        MembersRequestDto.UpdateOneForAttributeDto requestDto =
                (MembersRequestDto.UpdateOneForAttributeDto) joinPoint.getArgs()[2];

        if (requestDto.getPosition() != null && requestDto.getDepartment() != null) {
            String message = loginMember.getMemberName()
                    + "님이 "
                    + requestDto.getMemberName()
                    + "님의 부서, 직책을 "
                    + requestDto.getDepartment() + ", "
                    + requestDto.getPosition()
                    + "로 변경하였습니다.";

            commonAspect.getLogMessage(message);

            commonAspect.auditLogServiceSave(
                    AuditLogType.CHANGE_MEMBER_ATTRIBUTE, loginMember.getMemberName(), message,
                    loginMember.getDepartment(), loginMember.getPosition());
        }
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImplV1.updateOneForProfileImage(..))")
    public void logAfterUpdateMemberProfileImageUrl(JoinPoint joinPoint) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName()
                + "님이 파일을 업로드 하셨습니다.";

        commonAspect.getLogMessage(message);

        commonAspect.auditLogServiceSave(
                AuditLogType.UPLOAD_MEMBER_FILE, loginMember.getMemberName(), message,
                loginMember.getDepartment(), loginMember.getPosition());
    }
}
