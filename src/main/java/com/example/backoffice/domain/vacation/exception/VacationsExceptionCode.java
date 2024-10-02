package com.example.backoffice.domain.vacation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum VacationsExceptionCode {
    NOT_FOUND_VACATIONS(HttpStatus.BAD_REQUEST, "VACATION-001", "해당 휴가는 찾을 수 없습니다."),
    INVALID_START_DATE(HttpStatus.BAD_REQUEST, "VACATIONS-002", "시작 날짜가 잘못되었습니다."),
    END_DATE_BEFORE_START_DATE(HttpStatus.BAD_REQUEST, "VACATIONS-003", "종료 날짜가 시작 날짜보다 앞설 수 없습니다."),
    RESTRICTED_DATE_RANGE(HttpStatus.BAD_REQUEST, "VACATIONS-004", "이 날짜에는 일정을 만들 수 없습니다."),
    NO_PERMISSION_TO_CREATE_VACATION(HttpStatus.BAD_REQUEST, "VACATIONS-005",
            "해당 인원은 휴가를 생성할 수 없습니다. 다음 사항을 고려해주세요 :" +
                    " 이번달 휴가 신청은 하루에 한 번입니다." +
                    " 신청한 내역이 존재하다면 신청 취소를 하고 다시 신청해주세요"),
    INVALID_EVENT_CRUD_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "VACATIONS-006", "일정 처리 방식 오류입니다."),
    NO_PERMISSION_TO_DELETE_VACATION(HttpStatus.FORBIDDEN, "VACATIONS-007", "해당 부서의 일정을 삭제할 권한이 없습니다."),
    INVALID_START_DATE_OR_PRESS_URGENT_BUTTON(HttpStatus.BAD_REQUEST, "VACATIONS-008", "7일 이전의 휴가 요청은 '긴급함' 표시를 눌러주세요."),
    INVALID_URGENT(HttpStatus.BAD_REQUEST, "VACATIONS-009", "7일 이후의 휴가 요청은 '긴급함' 표시를 제거해주세요."),
    INSUFFICIENT_VACATION_DAYS(HttpStatus.BAD_REQUEST, "VACATIONS-010", "잔여 휴가 일수가 충분하지 않습니다."),
    INVALID_VACATION_DAYS(HttpStatus.BAD_REQUEST, "VACATIONS-011", "휴가를 30일 이상 설정할 수 없습니다."),
    NO_PERMISSION_TO_READ_VACATION(HttpStatus.FORBIDDEN, "VACATIONS-012", "휴가 일정을 읽을 권한이 없습니다."),
    EXCEEDS_VACATION_RATE_LIMIT(HttpStatus.BAD_REQUEST, "VACATIONS-013", "해당 날짜에 과도한 휴가 인원이 있어 제한합니다."),
    NO_PERMISSION_TO_UPDATE_VACATION(HttpStatus.FORBIDDEN, "VACATIONS-014", "다른 사람의 휴가 일정을 수정할 권한이 없습니다."),
    NOT_FOUND_VACATION_TYPE(HttpStatus.BAD_REQUEST, "VACATIONS-015", "해당 휴가 타입의 요청은 잘못된 요청입니다."),
    OUT_OF_VACATION_REQUEST_PERIOD(HttpStatus.BAD_REQUEST, "VACATION-016",
            "해당 기간은 휴가 신청 기간이 아닙니다." +
            " 또는 긴급함 표시를 눌러서 휴가 신청해주세요"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
