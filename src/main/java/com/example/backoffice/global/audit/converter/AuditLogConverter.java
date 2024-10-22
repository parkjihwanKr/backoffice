package com.example.backoffice.global.audit.converter;

import com.example.backoffice.global.audit.dto.AuditLogResponseDto;
import com.example.backoffice.global.audit.entity.AuditLog;
import com.example.backoffice.global.audit.entity.AuditLogType;
import com.example.backoffice.global.exception.AuditLogCustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import org.springframework.data.domain.Page;

public class AuditLogConverter {

    public static AuditLog toEntity(
            AuditLogType auditLogType, String memberName, String details){
        return AuditLog.builder()
                .auditLogType(auditLogType)
                .memberName(memberName)
                .details(details)
                .build();
    }

    public static AuditLogType toAuditType(String auditType) {
        return switch (auditType) {
            case AuditLogType.eventLabel.LOGIN -> AuditLogType.LOGIN;
            case AuditLogType.eventLabel.LOGOUT -> AuditLogType.LOGOUT;
            case AuditLogType.eventLabel.SIGNUP -> AuditLogType.SIGNUP;
            case AuditLogType.eventLabel.DELETE_MEMBER -> AuditLogType.DELETE_MEMBER;
            case AuditLogType.eventLabel.CHANGE_MEMBER_ATTRIBUTE -> AuditLogType.CHANGE_MEMBER_ATTRIBUTE;
            case AuditLogType.eventLabel.CHANGE_MEMBER_SALARY -> AuditLogType.CHANGE_MEMBER_SALARY;
            case AuditLogType.eventLabel.CHANGE_MEMBER_REMAINING_VACATION_DAY -> AuditLogType.CHANGE_MEMBER_REMAINING_VACATION_DAY;
            case AuditLogType.eventLabel.UPLOAD_MEMBER_FILE -> AuditLogType.UPLOAD_MEMBER_FILE;
            case AuditLogType.eventLabel.MEMBER_ERROR -> AuditLogType.MEMBER_ERROR;
            case AuditLogType.eventLabel.FILE_ERROR -> AuditLogType.FILE_ERROR;
            case AuditLogType.eventLabel.CREATE_FILE -> AuditLogType.CREATE_FILE;
            case AuditLogType.eventLabel.UPDATE_FILE -> AuditLogType.UPDATE_FILE;
            case AuditLogType.eventLabel.DELETE_FILE -> AuditLogType.DELETE_FILE;
            case AuditLogType.eventLabel.CREATE_MEMBER_VACATION -> AuditLogType.CREATE_MEMBER_VACATION;
            case AuditLogType.eventLabel.UPDATE_MEMBER_VACATION -> AuditLogType.UPDATE_MEMBER_VACATION;
            case AuditLogType.eventLabel.CHANGE_BOARD_FILE -> AuditLogType.CHANGE_BOARD_FILE;
            case AuditLogType.eventLabel.CHANGE_EVENT -> AuditLogType.CHANGE_EVENT;
            case AuditLogType.eventLabel.CHANGE_SECURITY_SETTINGS -> AuditLogType.CHANGE_SECURITY_SETTINGS;
            default -> {
                throw new AuditLogCustomException(GlobalExceptionCode.INVALID_VALUE);
            }
        };
    }

    public static Page<AuditLogResponseDto.ReadOneDto> toAuditLogPageDto(
            Page<AuditLog> auditLogPage){
        return auditLogPage.map(AuditLogConverter::toReadOneDto);
    }

    public static AuditLogResponseDto.ReadOneDto toReadOneDto(AuditLog auditLog){
        return AuditLogResponseDto.ReadOneDto.builder()
                .auditLogId(auditLog.getId())
                .auditLogType(auditLog.getAuditLogType())
                .createdAt(auditLog.getCreatedAt())
                .details(auditLog.getDetails())
                .memberName(auditLog.getMemberName())
                .build();
    }
}
