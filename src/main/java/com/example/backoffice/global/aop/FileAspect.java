package com.example.backoffice.global.aop;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacade;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.audit.service.AuditLogService;
import com.example.backoffice.global.exception.AWSCustomException;
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
public class FileAspect extends CommonAspect{

    private final AuditLogService auditLogService;
    private final MembersService membersService;
    private final NotificationsServiceFacade notificationsServiceFacade;

    @AfterThrowing(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesServiceImpl.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String errorMessage = createErrorMessage(joinPoint, error);

        // 파일은 AWS Exception 관련밖에 없음
        if (error instanceof AWSCustomException exception) {
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
    }

    // 파일 관련해서는 모든 로그를 찍도록 설정
    // createFileForMemberRole, createFileForBoard, createImage, deleteFile, deleteImage
    @AfterReturning(pointcut = "execution(* com.example.backoffice.domain.file.service.FilesServiceImpl.*(..))")
    public void logAfterAllMethod(JoinPoint joinPoint){
        String methodName = getCurrentMethodName(joinPoint);
        String message = "";
        if(methodName.equals("createFileForMemberRole")
                || methodName.equals("createFileForBoard") || methodName.equals("createImage")) {
            message = getLoginMemberName()+"님이 파일을 생성하셨습니다.";
            auditLogService.saveLogEvent(AuditLogType.CREATE_FILE, getLoginMemberName(), message);
        }else if(methodName.equals("deleteFile") || methodName.equals("deleteImage")){
            message = getLoginMemberName()+ "님이 파일을 삭제하셨습니다.";
            auditLogService.saveLogEvent(AuditLogType.DELETE_FILE, getLoginMemberName(), message);
        }else{
            message = getLoginMemberName()+ "님이 알 수 없는 메서드를 불렀습니다.";
            auditLogService.saveLogEvent(AuditLogType.FILE_ERROR, getLoginMemberName(), message);
        }
    }
}
