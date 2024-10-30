package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class DateCustomException extends CustomException {
    public DateCustomException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
