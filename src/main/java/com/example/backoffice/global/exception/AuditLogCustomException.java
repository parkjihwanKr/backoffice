package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class AuditLogCustomException extends CustomException{
    public AuditLogCustomException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
