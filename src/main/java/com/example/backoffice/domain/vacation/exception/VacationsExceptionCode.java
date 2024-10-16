package com.example.backoffice.domain.vacation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VacationsExceptionCode {
    NOT_FOUND_VACATIONS(HttpStatus.BAD_REQUEST, "VACATION-001", "해당 휴가를 찾을 수 없습니다."),
    INVALID_START_DATE(HttpStatus.BAD_REQUEST, "VACATIONS-002", "휴가 시작일이 내일 이후여야 합니다."),
    END_DATE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "VACATIONS-003", "종료 날짜는 시작 날짜보다 이전일 수 없습니다."),
    RESTRICTED_DATE_RANGE(HttpStatus.BAD_REQUEST, "VACATIONS-004", "해당 날짜에는 휴가를 신청할 수 없습니다."),
    NO_PERMISSION_TO_CREATE_VACATION(HttpStatus.BAD_REQUEST, "VACATIONS-005",
            "휴가 생성 권한이 없습니다. 같은 달에 이미 휴가가 신청되었습니다. 휴가를 취소하고 다시 신청해주세요."),
    INVALID_EVENT_CRUD_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "VACATIONS-006", "잘못된 일정 처리 요청입니다."),
    NO_PERMISSION_TO_DELETE_VACATION(HttpStatus.FORBIDDEN, "VACATIONS-007", "해당 일정을 삭제할 권한이 없습니다."),
    INVALID_START_DATE_OR_PRESS_URGENT_BUTTON(HttpStatus.BAD_REQUEST, "VACATIONS-008",
            "7일 이전 휴가는 '긴급함' 표시가 필요합니다."),
    INVALID_URGENT(HttpStatus.BAD_REQUEST, "VACATIONS-009", "7일 이후 휴가는 '긴급함' 표시를 해제해주세요."),
    DO_NOT_NEED_URGENT(HttpStatus.BAD_REQUEST, "VACATIONS-010", "연가는 '긴급함' 표시가 필요하지 않습니다."),
    INSUFFICIENT_VACATION_DAYS(HttpStatus.BAD_REQUEST, "VACATIONS-011", "잔여 휴가 일수가 부족합니다."),
    INVALID_VACATION_DAYS(HttpStatus.BAD_REQUEST, "VACATIONS-012", "휴가는 30일 이상 신청할 수 없습니다."),
    NO_PERMISSION_TO_READ_VACATION(HttpStatus.FORBIDDEN, "VACATIONS-013", "휴가 일정을 조회할 권한이 없습니다."),
    EXCEEDS_VACATION_RATE_LIMIT(HttpStatus.BAD_REQUEST, "VACATIONS-014", "해당 날짜에 휴가 인원이 많아 신청이 제한됩니다."),
    NO_PERMISSION_TO_UPDATE_VACATION(HttpStatus.FORBIDDEN, "VACATIONS-015", "다른 사람의 휴가 일정을 수정할 권한이 없습니다."),
    NOT_FOUND_VACATION_TYPE(HttpStatus.BAD_REQUEST, "VACATIONS-016", "잘못된 휴가 타입 요청입니다."),
    OUT_OF_VACATION_REQUEST_PERIOD(HttpStatus.BAD_REQUEST, "VACATION-017",
            "해당 기간은 휴가 신청 기간이 아닙니다. 긴급함 표시를 눌러 휴가를 신청해주세요."),
    OVERLAPPING_VACATION_DATES(HttpStatus.BAD_REQUEST, "VACATIONS-018",
            "휴가 날짜가 기존 일정과 겹칩니다. 다른 날짜로 신청해주세요."),
    NEED_URGENT(HttpStatus.BAD_REQUEST, "VACATIONS-019", "연가를 제외한 휴가는 '긴급함' 표시가 필요합니다."),
    INVALID_START_DATE_WEEKEND(HttpStatus.BAD_REQUEST, "VACATIONS-020", "휴가 시작날은 토요일/일요일은 불가능합니다."),
    INVALID_END_DATE_WEEKEND(HttpStatus.BAD_REQUEST, "VACATIONS-021", "휴가 마지막날은 토요일/일요일은 불가능합니다."),
    NOT_FOUND_VACATION_IS_ACCEPTED_TYPE(HttpStatus.BAD_REQUEST, "VACATIONS-022", "잘못된 휴가 필터 요청입니다."),
    INVALID_VACATION_PERIOD(HttpStatus.BAD_REQUEST, "VACATIONS-023","적절한 휴가 신청 기간으로 설정해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
