package com.example.backoffice.domain.event.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventsExceptionCode {

    NOT_FOUND_EVENT(HttpStatus.BAD_REQUEST, "EVENT-001", "해당 이벤트를 찾을 수 없습니다."),
    INVALID_START_DATE(HttpStatus.BAD_REQUEST, "EVENT-002", "휴가의 시작날이 잘못되었습니다."),
    END_DATE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "EVENT-003", "휴가의 시작 날이 마지막 날보다 느릴 수 없습니다."),
    RESTRICTED_DATE_RANGE(HttpStatus.BAD_REQUEST, "EVENT-004", "휴가를 나갈 수 없는 날입니다."),
    INVALID_DEPARTMENT(HttpStatus.BAD_REQUEST, "EVENT-005", "해당 멤버는 해당 부서의 이벤트를 생성할 수 없습니다."),
    INVALID_EVENT_CRUD_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "EVENT-006","해당 이벤트 처리 방식 오류입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
