package com.example.backoffice.domain.admin.exception;

import com.example.backoffice.global.exception.CustomException;

public class AdminCustomException extends CustomException {
    public AdminCustomException(AdminExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
