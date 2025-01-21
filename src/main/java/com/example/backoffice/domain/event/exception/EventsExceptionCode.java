package com.example.backoffice.domain.event.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventsExceptionCode {

    NOT_FOUND_EVENT(
            HttpStatus.BAD_REQUEST, "EVENT-001",
            "해당 이벤트를 찾을 수 없습니다."),
    INVALID_START_DATE(
            HttpStatus.BAD_REQUEST, "EVENT-002",
            "시작 날짜가 잘못되었습니다."),
    END_DATE_BEFORE_START_DATE(
            HttpStatus.BAD_REQUEST, "EVENT-003",
            "종료 날짜가 시작 날짜보다 앞설 수 없습니다."),
    NO_PERMISSION_TO_CREATE_EVENT(
            HttpStatus.FORBIDDEN, "EVENT-005",
            "해당 부서의 일정을 생성할 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_EVENT(
            HttpStatus.FORBIDDEN, "EVENT-007",
            "해당 부서의 일정을 삭제할 권한이 없습니다."),
    INVALID_START_DATE_OR_PRESS_URGENT_BUTTON(
            HttpStatus.BAD_REQUEST, "EVENT-008",
            "7일 이전의 휴가 요청은 '긴급함' 표시를 눌러주세요."),
    NO_PERMISSION_TO_READ_EVENT(
            HttpStatus.FORBIDDEN, "EVENT-012",
            "휴가 일정을 읽을 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
