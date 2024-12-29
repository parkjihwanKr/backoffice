package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class JsonCustomException extends CustomException {
    public JsonCustomException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
