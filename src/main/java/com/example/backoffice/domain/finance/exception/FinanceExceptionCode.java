package com.example.backoffice.domain.finance.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FinanceExceptionCode {
    NOT_FOUND_FINANCE(HttpStatus.BAD_REQUEST, "FINANCE-001", "해당 지출 관리 시스템을 찾을 수 없습니다."),
    RESTRICTED_ACCESS(HttpStatus.BAD_REQUEST,"FINANCE-002", "해당 멤버는 접근 권한이 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
