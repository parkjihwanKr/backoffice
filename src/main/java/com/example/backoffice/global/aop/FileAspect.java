package com.example.backoffice.global.aop;

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
public class FileAspect {

    private final AuditLogService auditLogService;
    private final CommonAspect commonAspect;

    /*    String createOneForMemberRole(MultipartFile file, Members member);

    String createOneForBoard(MultipartFile file, Boards board);

    String createImage(MultipartFile image);

    String createOneForEvent(MultipartFile file, Events event);

    Files createOneForExpense(
            MultipartFile file, Expense expense, Members loginMember);

    void deleteForBoard(Long boardId, List<String> fileList);

    void deleteForEvent(Long eventId, List<String> fileList);

    void deleteForExpense(Long expenseId, List<String> fileList);

    void deleteImage(String imageUrl);*/
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesServiceV1.*(..))")
    public void logAfterAllMethod(JoinPoint joinPoint) {
        String methodName = commonAspect.getCurrentMethodName(joinPoint);
        String message = "";
        String loginMemberName = commonAspect.getLoginMemberName();
        if (methodName.equals("createOneForMemberRole")
                || methodName.equals("createOneForBoard") || methodName.equals("createImage")
                || methodName.equals("createOneForEvent") || methodName.equals("createOneForExpense")) {
            message = loginMemberName + "님이 파일을 생성하셨습니다.";
            auditLogService.save(
                    AuditLogType.CREATE_FILE, loginMemberName, message);
        } else if (methodName.equals("deleteForBoard") || methodName.equals("deleteImage")
                || methodName.equals("deleteForEvent") || methodName.equals("deleteForExpense")) {
            message = loginMemberName + "님이 파일을 삭제하셨습니다.";
            auditLogService.save(
                    AuditLogType.DELETE_FILE, loginMemberName, message);
        } else {
            message = loginMemberName + "님이 알 수 없는 메서드를 불렀습니다.";
            auditLogService.save(
                    AuditLogType.FILE_ERROR, loginMemberName, message);
        }
        commonAspect.getLogMessage(message);
    }
}
