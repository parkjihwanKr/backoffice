package com.example.backoffice.domain.event.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventsExceptionCode {

    NOT_FOUND_EVENT(HttpStatus.BAD_REQUEST, "EVENT-001", "해당 이벤트를 찾을 수 없습니다."),
    INVALID_START_DATE(HttpStatus.BAD_REQUEST, "EVENT-002", "시작 날짜가 잘못되었습니다."),
    END_DATE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "EVENT-003", "종료 날짜가 시작 날짜보다 앞설 수 없습니다."),
    RESTRICTED_DATE_RANGE(HttpStatus.BAD_REQUEST, "EVENT-004", "이 날짜에는 일정을 만들 수 없습니다."),
    NO_PERMISSION_TO_CREATE_EVENT(HttpStatus.FORBIDDEN, "EVENT-005", "해당 부서의 일정을 생성할 권한이 없습니다."),
    INVALID_EVENT_CRUD_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "EVENT-006", "일정 처리 방식 오류입니다."),
    NO_PERMISSION_TO_DELETE_EVENT(HttpStatus.FORBIDDEN, "EVENT-007", "해당 부서의 일정을 삭제할 권한이 없습니다."),
    INVALID_START_DATE_OR_PRESS_URGENT_BUTTON(HttpStatus.BAD_REQUEST, "EVENT-008", "7일 이전의 휴가 요청은 '긴급함' 표시를 눌러주세요."),
    INVALID_URGENT(HttpStatus.BAD_REQUEST, "EVENT-009", "7일 이후의 휴가 요청은 '긴급함' 표시를 제거해주세요."),
    INSUFFICIENT_VACATION_DAYS(HttpStatus.BAD_REQUEST, "EVENT-010", "잔여 휴가 일수가 충분하지 않습니다."),
    INVALID_VACATION_DAYS(HttpStatus.BAD_REQUEST, "EVENT-011", "휴가를 30일 이상 설정할 수 없습니다."),
    NO_PERMISSION_TO_READ_EVENT(HttpStatus.FORBIDDEN, "EVENT-012", "휴가 일정을 읽을 권한이 없습니다."),
    EXCEEDS_VACATION_RATE_LIMIT(HttpStatus.BAD_REQUEST, "EVENT-013", "해당 날짜에 과도한 휴가 인원이 있어 제한합니다."),
    NO_PERMISSION_TO_UPDATE_EVENT(HttpStatus.FORBIDDEN, "EVENT-014", "다른 사람의 휴가 일정을 수정할 권한이 없습니다."),
    NOT_MATCHED_EVENT_TYPE(HttpStatus.BAD_REQUEST, "EVENT-015", "해당 이벤트 타입에 맞지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
