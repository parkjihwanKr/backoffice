package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class DateUtilException extends CustomException {
    public DateUtilException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
