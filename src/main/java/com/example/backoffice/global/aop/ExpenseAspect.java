package com.example.backoffice.global.aop;

import com.example.backoffice.domain.expense.dto.ExpenseResponseDto;
import com.example.backoffice.domain.expense.exception.ExpenseCustomException;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
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
public class ExpenseAspect {

    private final AuditLogService auditLogService;
    private final CommonAspect commonAspect;

    @Pointcut("execution(* com.example.backoffice.domain.expense.service.ExpenseServiceImplV1.*(..))")
    public void expenseServiceMethods() {}

    // 공통 메서드: 로그 메시지를 생성하고 저장
    private void logAudit(
            String memberName, String title, String action,
            AuditLogType auditLogType, MemberDepartment department,
            MemberPosition position) {
        String message = memberName + "님이 " + title + "의 지출 내역서를 " + action + "하셨습니다.";
        commonAspect.getLogMessage(message);
        auditLogService.save(
                auditLogType, memberName, message, department, position);
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.expense.service.ExpenseServiceImplV1.createOne(..))", returning = "result")
    public void logAfterCreateOne(JoinPoint joinPoint, ExpenseResponseDto.CreateOneDto result) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        logAudit(loginMember.getMemberName(), result.getTitle(),
                "제출", AuditLogType.CREATE_EXPENSE_REPORT,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.expense.service.ExpenseServiceImplV1.updateOneForProcess(..))", returning = "result")
    public void logAfterUpdateOneForProcess(JoinPoint joinPoint, ExpenseResponseDto.UpdateOneForProcessDto result) {
        String process = (String) joinPoint.getArgs()[2];
        Members loginMember = (Members) joinPoint.getArgs()[3];
        logAudit(loginMember.getMemberName(), result.getTitle(), process, AuditLogType.UPDATE_EXPENSE_REPORT_STATUS,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.expense.service.ExpenseServiceImplV1.readOne(..))", returning = "result")
    public void logAfterReadOne(JoinPoint joinPoint, ExpenseResponseDto.ReadOneDto result) {
        Members loginMember = (Members) joinPoint.getArgs()[1];
        logAudit(loginMember.getMemberName(), result.getTitle(), "조회", AuditLogType.READ_EXPENSE_REPORT,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.expense.service.ExpenseServiceImplV1.deleteOne(..))")
    public void logAfterDeleteOne(JoinPoint joinPoint) {
        Long expenseId = (Long) joinPoint.getArgs()[0];
        Members loginMember = (Members) joinPoint.getArgs()[1];
        String message = loginMember.getMemberName() + "님이 " + expenseId + "의 지출 내역서를 삭제하셨습니다.";
        commonAspect.getLogMessage(message);
        auditLogService.save(AuditLogType.DELETE_EXPENSE_REPORT, loginMember.getMemberName(), message,
                loginMember.getDepartment(), loginMember.getPosition());
    }

    @AfterThrowing(pointcut = "expenseServiceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, ExpenseCustomException ex) {
        Members member = commonAspect.getLoginMemberInfo();
        String methodName = commonAspect.getCurrentMethodName(joinPoint); // 메서드 이름 가져오기

        if(member == null){
            String anonymousUser = "anonymousUser";
            String message = "Error in method: " + methodName + " | "
                    + anonymousUser+" 님이 "
                    + "지출 내역 처리 중 오류가 발생했습니다. "
                    + "Exception: " + ex.getMessage()
                    + "추가적인 조치가 필요합니다.";

            commonAspect.getLogMessage(message);
            auditLogService.save(AuditLogType.EXPENSE_REPORT_ERROR, anonymousUser, message,
                    null, null);
            return;
        }
        String message = "Error in method: " + methodName + " | "
                + member.getMemberName() + "님이 "
                + "지출 내역 처리 중 오류가 발생했습니다. "
                + "Exception: " + ex.getMessage();

        commonAspect.getLogMessage(message);
        auditLogService.save(AuditLogType.EXPENSE_REPORT_ERROR, member.getMemberName(), message,
                member.getDepartment(), member.getPosition());
    }
}
