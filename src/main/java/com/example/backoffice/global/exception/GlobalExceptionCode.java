package com.example.backoffice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {
    // AWS S3
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "값이 유효하지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON-002", "파라미터가 누락되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-003", "내부 서버 에러 입니다."),

    // AWS S3
    AWS_S3_FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "S3-001", "파일 업로드에 실패하셨습니다."),
    AWS_S3_FILE_NAME_IS_BLANK(HttpStatus.BAD_REQUEST, "S3-002", "파일 이름은 공백일 수 없습니다."),
    AWS_S3_NOT_MATCHED_FILE_URL(HttpStatus.BAD_REQUEST, "S3-003", "파일 이름 형식 오류입니다."),

    // Validate JWT Token
    INVALID_TOKEN_VALUE(HttpStatus.BAD_REQUEST, "JWT-001", "JWT 토큰 값이 유효하지 않습니다."),
    UNAUTHORIZED_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "JWT-002", "JWT Refresh 토큰값이 유효하지 않습니다."),
    INVALID_SIGNATURE(HttpStatus.BAD_REQUEST, "JWT-003","Invalid JWT signature, 유효하지 않는 JWT 서명 입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.BAD_REQUEST,"JWT-004","Expired JWT token, 만료된 JWT token 입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "JWT-005", "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다."),

    // Authentcation
    NOT_MATCHED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "AUTH-001", "해당 인증 정보가 일치하지 않습니다."),

    // JSON
    NOT_SERIALIZED_JSON(HttpStatus.BAD_REQUEST, "JSON-001", "직렬화가 되지 않습니다."),
    NOT_DESERIALIZED_JSON(HttpStatus.BAD_REQUEST, "JSON-002", "역직렬화가 되지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
