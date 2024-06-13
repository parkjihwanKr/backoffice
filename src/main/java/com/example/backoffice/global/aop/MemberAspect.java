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
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MemberAspect extends CommonAspect{

    private final AuditLogService auditLogService;
    private final MembersService membersService;
    private final NotificationsServiceFacade notificationsServiceFacade;
    /* @PARAM
    JoinPoint -> 각 메서드들의 파라미터들을 가져옴
    joinPoint.getArg[0] -> 첫 번째 파라미터 ...
    jointPoint.getArg[n] -> n-1 번째 파라미터

    @Before : 메서드 실행 전
    @After : 메서드 실행 후
    @AfterReturning : 메서드가 정상적으로 종료된 후
    @AfterThrowing : 메서드가 예외 처리가 된 후
    */

    @Before("execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("실행 중: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("실행 후 : " + joinPoint.getSignature().toShortString() + ", 결과 : " + result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String errorMessage
                = "예외 : " + getCurrentMethodName(joinPoint) +" "+ error;
        log.error(errorMessage, error);

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

        String loginMemberName = getLoginMemberName();
        auditLogService.saveLogEvent(
                AuditLogType.MEMBER_ERROR, loginMemberName, errorMessage);
    }

    // 각 멤버의 로그인이 아니라 Authentication.authenticate()는
    // 인증된 사용자가 제대로 된 url로 접속해 들어가는지를 확인
    // 즉, @AuthenticationPrincipal MemberDetails에 해당하는 적절한 값이 들어간 사용자에 대한 로그 기록
    // AOP는 같은 패키지 내에 protected 메서드에 접근할 수 없음.
    /*@AfterReturning(pointcut = "execution(* org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public void logAfterLogin(JoinPoint joinPoint) {
        String username = (String) joinPoint.getArgs()[0];
        auditLogService.saveLogEvent(
                AuditLogType.LOGIN, username, username + "님이 로그인하셨습니다.");
    }*/

    // 각 멤버의 로그 아웃
    /*@AfterReturning("execution(* org.springframework.security.web.authentication.logout.LogoutHandler.logout(..))")
    public void logAfterLogout(JoinPoint joinPoint) {
        String username = (String) joinPoint.getArgs()[0];
        auditLogService.saveLogEvent(
                AuditLogType.LOGOUT, username, username + "님이 로그아웃하셨습니다.");
    }*/

    // JoinPoint @Param MembersRequestDto.CreateMembersRequestDto requestDto
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.signup(..))")
    public void logAfterSignup(JoinPoint joinPoint){
        MembersRequestDto.CreateMembersRequestDto requestDto
                = (MembersRequestDto.CreateMembersRequestDto) joinPoint.getArgs()[0];
        String message = requestDto.getMemberName()+ "님이 회원가입을 진행하셨습니다.";
        log.info(message);
        auditLogService.saveLogEvent(
                AuditLogType.SIGNUP, requestDto.getMemberName(), message);
    }

    /*
    JoinPoint @Param
    Long toMemberId, Members loginMember, MembersRequestDto.UpdateMemberSalaryRequestDto requestDto
    */
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.updateSalary(..))")
    public void logAfterUpdateSalary(JoinPoint joinPoint){
        Members loginMember = (Members) joinPoint.getArgs()[1];
        MembersRequestDto.UpdateMemberSalaryRequestDto requestDto =
                (MembersRequestDto.UpdateMemberSalaryRequestDto) joinPoint.getArgs()[2];
        String message
                = loginMember.getMemberName()+"님이 "
                + requestDto.getMemberName()+"님의 급여를 "
                + requestDto.getSalary()+"로 변경하셨습니다.";

        auditLogService.saveLogEvent(
                AuditLogType.CHANGE_MEMBER_SALARY,
                loginMember.getMemberName(), message);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.deleteMember(..))")
    public void logAfterDeleteMember(JoinPoint joinPoint){
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName() + "님이 회원 탈퇴하셨습니다.";

        auditLogService.saveLogEvent(
                AuditLogType.DELETE_MEMBER,
                loginMember.getMemberName(), message);
    }

    /*@PARAM
    Long memberId, Members loginMember,
            MembersRequestDto.UpdateMemberAttributeRequestDto requestDto*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.updateAttribute(..))")
    public void logAfterUpdateMemberAttribute(JoinPoint joinPoint){
        Members loginMember = (Members) joinPoint.getArgs()[1];
        MembersRequestDto.UpdateMemberAttributeRequestDto requestDto =
                (MembersRequestDto.UpdateMemberAttributeRequestDto)
                        joinPoint.getArgs()[2];

        if(requestDto.getSalary() != null
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

    // 개인이 올린 사진이 회사 방침 내에서 올바른지?, 삭제는 필요 없을 듯?
    /*@PARAM
    Long memberId, Members member, MultipartFile image*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.member.facade.MembersServiceFacadeImpl.updateProfileImageUrl(..))")
    public void logAfterUpdateMemberProfileImageUrl(JoinPoint joinPoint){
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName()
                + "님이 파일을 업로드 하셨습니다.";
        auditLogService.saveLogEvent(
                AuditLogType.UPLOAD_MEMBER_FILE,
                loginMember.getMemberName(), message);
    }
}
