package com.example.backoffice.domain.attendance.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendancesExceptionCode {
    NOT_FOUND_ATTENDANCES(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-001",
            "해당 근태 기록은 찾을 수 없습니다."),
    TIME_BEFORE_TODAY(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-002",
            "요청 받은 시간이 오늘 이전일 수 없습니다."),
    TIME_EQUAL_OR_AFTER_TOMORROW(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-003",
            "요청 받은 시간이 내일이거나 이후일 수 없습니다."),
    CHECK_IN_TIME_OUTSIDE_WORK_HOURS(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-004",
            "출근 시간이 유효한 시간은 오전 8시부터입니다."),
    RESTRICTED_ACCESS(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-005",
            "해당 출결 기록을 접근할 수 없습니다."),
    NOT_FOUND_ATTENDANCE_STATUS(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-006",
            "해당하는 출결 상태는 존재하지 않습니다"),
    EQUALS_TO_ATTENDANCES_STATUS_AND_DESCRIPTION(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-007",
            "요청하신 출결 기록은 적용하시려는 출결의 상태와 설명이 같습니다."),
    DUPLICATED_ATTENDANCE(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-008",
            "요청하신 날짜에 멤버의 근태 기록이 있습니다. 해당 부분을 삭제한 뒤," +
                    " 수동 작성해주세요."),
    IS_HOLIDAY(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-009",
            "요청하신 날짜는 휴일입니다."),
    CHECK_OUT_TIME_OUTSIDE_WORK_HOURS(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-010",
            "퇴근 시간이 유효한 시간은 오후 5시 반부터 오후 7시부터입니다."),
    TIME_BEFORE_HALF_DAY(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-011",
            "해당 시간은 허용된 조퇴 시간이 아닙니다." +
                    "1시 이후 또는 해당 관리자에게 문의해주세요."),
    EXIST_CHECKOUT_TIME(
            HttpStatus.BAD_REQUEST, "ATTENDANCES-012",
            "퇴근 요청을 이미 하셨습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
