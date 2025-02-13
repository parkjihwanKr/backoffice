package com.example.backoffice.domain.vacation.exception;

import com.example.backoffice.global.exception.CustomException;

public class VacationsCustomException extends CustomException {
    public VacationsCustomException(VacationsExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
