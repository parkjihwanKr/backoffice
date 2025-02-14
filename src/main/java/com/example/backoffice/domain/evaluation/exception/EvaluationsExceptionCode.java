package com.example.backoffice.domain.evaluation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EvaluationsExceptionCode {
    NOT_FOUND_EVALUATIONS(
            HttpStatus.BAD_REQUEST, "EVALUATIONS-0O1",
            "요청한 평가를 찾을 수 없습니다."),
    UNAUTHORIZED_DEPARTMENT_ACCESS(
            HttpStatus.FORBIDDEN, "EVALUATIONS-002",
            "해당 부서는 요청한 설문조사를 할 수 없습니다."),
    END_DATE_BEFORE_START_DATE(
            HttpStatus.BAD_REQUEST,"EVALUATIONS-003",
            "요청하신 시작 날짜는 마감 날짜보다 빠릅니다."),
    MINIMUM_DURATION_TOO_SHORT(
            HttpStatus.BAD_REQUEST,"EVALUATION-004",
            "요청하신 설문 조사 기간은 최소 7일입니다."),
    END_DATE_TOO_LATE(
            HttpStatus.BAD_REQUEST,"EVALUATIONS-005",
            "요청하신 마감 날짜는 분기 마지막 달 마지막 일로부터 14일 전으로 설정해야합니다."),
    INCORRECT_DATE_QUARTER_REQUEST(
            HttpStatus.BAD_REQUEST, "EVALUATIONS-006",
            "요청하신 시작날의 분기와 마감일의 분기가 다릅니다."),
    QUARTER_CALCULATE_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR, "EVALUATION-007",
            "분기 계산 에러"),
    NOW_DATE_BEFORE_START_DATE(
            HttpStatus.BAD_REQUEST,"EVALUATIONS-008",
            "요청하신 시작 날짜는 지났습니다."),
    UNAUTHORIZED_ACCESS(
            HttpStatus.FORBIDDEN,"EVALUATIONS-009",
            "연간 설문 조사를 작성할 권한이 없습니다."),
    IS_NOT_YAER_EQUAL_TO_NOW(
            HttpStatus.BAD_REQUEST, "EVALUATIONS-011",
            "시작 연도 또는 마감 연도는 현재 년도와 같지 않습니다."),
    NOT_FOUND_EVALUATION_TYPE(
            HttpStatus.BAD_REQUEST, "EVALUATIONS-012",
            "존재하지 않은 평가 타입입니다."),
    NOT_INPUT_EVALUATION_CHECKED(
            HttpStatus.FORBIDDEN,"EVALUATIONS-013",
            "해당 설문조사에 체크를 해야 제출이 됩니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
