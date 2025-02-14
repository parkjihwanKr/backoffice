package com.example.backoffice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {
    // COMMON
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "값이 유효하지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-002", "파라미터가 누락되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-003", "내부 서버 에러 입니다."),

    // AWS S3
    AWS_S3_FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "S3-001", "파일 업로드에 실패하셨습니다."),
    AWS_S3_FILE_NAME_IS_BLANK(HttpStatus.BAD_REQUEST, "S3-002", "파일 이름은 공백일 수 없습니다."),
    AWS_S3_NOT_MATCHED_FILE_URL(HttpStatus.BAD_REQUEST, "S3-003", "파일 이름 형식 오류입니다."),
    AWS_S3_FILE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "S3-004", "파일 삭제를 실패하셨습니다."),

    // Validate JWT Token
    INVALID_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "JWT-001", "JWT 토큰 값이 유효하지 않습니다."),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, "JWT-002", "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-003", "Expired JWT token, 만료된 JWT token 입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT-004", "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "JWT-005", "요청에 JWT token이 포함되지 않습니다."),
    NOT_EXIST_JWT_STATUS(HttpStatus.BAD_REQUEST, "JWT-006", "존재하지 않는 JWT STATUS입니다."),
    TOKEN_VALUE_IS_NULL(HttpStatus.BAD_REQUEST, "JWT-007", "토큰 값이 null입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "JWT-008",
            "Refresh Token이 Redis에 존재하지 않습니다." +
                    "다시 로그인해주세요."),

    // Authentcation
    NOT_MATCHED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "AUTH-001", "해당 인증 정보가 일치하지 않습니다."),

    // JSON
    NOT_SERIALIZED_JSON(HttpStatus.BAD_REQUEST, "JSON-001", "직렬화가 되지 않습니다."),
    NOT_DESERIALIZED_JSON(HttpStatus.BAD_REQUEST, "JSON-002", "역직렬화가 되지 않습니다."),

    // Scheduler
    NOT_FOUND_SCHEDULER_EVENT_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "SCHEDULER-001", "해당 스케줄러에 없는 이벤트 타입입니다."),
    NOT_FOUND_SCHEDULER_STRATEGY_NAME(HttpStatus.INTERNAL_SERVER_ERROR, "SCHEDULER-002", "해당 스케줄러에 없는 전략 타입입니다."),

    // AuditLog
    NOT_FOUND_AUDIT_LOG(HttpStatus.BAD_REQUEST, "AUDIT_LOG-001", "해당 감사 로그를 찾을 수 없습니다."),
    NOT_MATCHED_AUDIT_LOG_TYPE(HttpStatus.BAD_REQUEST, "AUDIT-LOG-002", "해당 감사 로그 타입을 찾을 수 없습니다."),

    // UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.BAD_REQUEST, "WEB-SOCKET-001", "해당 유저는 웹소캣 권한이 없습니다."),

    // DateUtil
    NOT_PARSE_DATE(HttpStatus.BAD_REQUEST, "DATE-001", "잘못된 날짜 형식입니다. "),
    START_DATE_AFTER_END_DATE(HttpStatus.BAD_REQUEST, "DATE-002", "시작일이 종료일 이후 일 수 없습니다. "),
    NOT_RETRIEVE_DATA(HttpStatus.BAD_REQUEST,"DATE-003", "일정을 조회할 수 없는 년 또는 달입니다."),
    NOT_EXIST_MONTH(HttpStatus.BAD_REQUEST,"DATE-004", "존재하지 않은 달입니다."),
    INVALID_YEAR(HttpStatus.BAD_REQUEST,"DATE-005", "알맞지 않은 년도를 요청하셨습니다."),
    INVALID_MONTH(HttpStatus.BAD_REQUEST,"DATE-006", "알맞지 않은 달을 요청하셨습니다."),
    BEFORE_TODAY(HttpStatus.BAD_REQUEST,"DATE-007", "오늘 이전의 날을 설정하셨습니다."),
    NOT_EQUAL_TO_NOW_YAER(HttpStatus.BAD_REQUEST,"DATE-008", "현재 날짜의 년도와 일치하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
