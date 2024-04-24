package com.example.backoffice.global.exception;


import lombok.Getter;

@Getter
public class JwtCustomException extends CustomException {
    public JwtCustomException(GlobalExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
