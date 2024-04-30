package com.example.backoffice.global.exception;

import lombok.Getter;

@Getter
public class AuthenticationCustomException extends CustomException{
    public AuthenticationCustomException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
