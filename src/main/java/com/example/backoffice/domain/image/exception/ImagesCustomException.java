package com.example.backoffice.domain.image.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImagesCustomException {
    UNSUPPORTED_EXTENSION(HttpStatus.BAD_REQUEST, "IMAGE-001", "해당 이미지 파일은 지원하지 않는 확장자입니다."),

    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
