package com.example.backoffice.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode {
    // AWS S3
    AWS_S3_FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "S3-001", "파일 업로드에 실패하셨습니다."),
    AWS_S3_FILE_NAME_IS_BLANK(HttpStatus.BAD_REQUEST, "S3-002", "파일 이름은 공백일 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
