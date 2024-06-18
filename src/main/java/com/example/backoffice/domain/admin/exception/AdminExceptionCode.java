package com.example.backoffice.domain.admin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum AdminExceptionCode {
    NOT_FOUND_ADMIN(HttpStatus.BAD_REQUEST, "ADMIN-001", "해당 관리자를 찾을 수 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
