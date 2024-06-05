package com.example.backoffice.domain.event.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventsExceptionCode {

    NOT_FOUND_EVENT(HttpStatus.BAD_REQUEST, "EVENT-001", "해당 이벤트를 찾을 수 없습니다."),
    INVALID_START_DATE(HttpStatus.BAD_REQUEST, "EVENT-002", "회사 일정의 시작날이 잘못되었습니다."),
    END_DATE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "EVENT-003", "회사 일정의 시작 날이 마지막 날보다 느릴 수 없습니다."),
    RESTRICTED_DATE_RANGE(HttpStatus.BAD_REQUEST, "EVENT-004", "회사 일정을 만들 수 없는 날입니다."),
    NO_PERMISSION_TO_CREATE_EVENT(HttpStatus.BAD_REQUEST, "EVENT-005", "해당 멤버는 해당 부서의 일정를 생성할 수 없습니다."),
    INVALID_EVENT_CRUD_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "EVENT-006","해당 일정 처리 방식 오류입니다."),
    NO_PERMISSION_TO_DELETE_EVENT(HttpStatus.FORBIDDEN, "EVENT-007", "해당 멤버는 해당 부서의 일정을 삭제할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
