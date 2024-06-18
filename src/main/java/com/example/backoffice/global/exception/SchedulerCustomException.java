package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class SchedulerCustomException extends CustomException{

    public SchedulerCustomException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }

}
