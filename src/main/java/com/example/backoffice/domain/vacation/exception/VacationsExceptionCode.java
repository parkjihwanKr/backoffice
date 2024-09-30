package com.example.backoffice.domain.vacation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VacationsExceptionCode {
    NOT_FOUND_VACATIONS(HttpStatus.BAD_REQUEST, "VACATION-001", "해당 휴가는 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
