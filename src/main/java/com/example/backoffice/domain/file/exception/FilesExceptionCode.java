package com.example.backoffice.domain.file.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FilesExceptionCode {
    UNSUPPORTED_EXTENSION(
            HttpStatus.BAD_REQUEST, "FILE-001",
            "해당 이미지 파일은 지원하지 않는 확장자입니다."),
    FILE_ENTITY_DELETE_FAIL(
            HttpStatus.BAD_REQUEST, "FILE-002",
            "파일 데이터 베이스에서 행 삭제 실패"),
    NEED_FILE(
            HttpStatus.BAD_REQUEST, "FILE-003",
            "파일 등록이 필수입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
