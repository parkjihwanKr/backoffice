package com.example.backoffice.domain.attendance.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendancesExceptionCode {
    NOT_FOUND_ATTENDANCES(HttpStatus.BAD_REQUEST, "ATTENDANCES-001", "해당 근태 기록은 찾을 수 없습니다."),
    TIME_BEFORE_TODAY(HttpStatus.BAD_REQUEST, "ATTENDANCES-002", "요청 받은 시간이 오늘 이전일 수 없습니다."),
    TIME_EQUAL_OR_AFTER_TOMORROW(HttpStatus.BAD_REQUEST, "ATTENDANCES-003", "요청 받은 시간이 내일이거나 이후일 수 없습니다."),
    CHECK_IN_TIME_OUTSIDE_WORK_HOURS(HttpStatus.BAD_REQUEST, "ATTENDANCES-004", "출근 시간이 유효한 시간은 오전 8시부터입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
