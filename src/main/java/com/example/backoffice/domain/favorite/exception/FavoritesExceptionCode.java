package com.example.backoffice.domain.favorite.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FavoritesExceptionCode {
    NOT_FOUND_FAVORITES(HttpStatus.BAD_REQUEST, "FAVORITES-001", "해당 즐겨찾기를 찾을 수 없습니다."),
    NO_PERMISSION_TO_READ_FAVORITE(HttpStatus.FORBIDDEN, "FAVORITES-002", "해당 즐겨찾기를 읽을 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_FAVORITE(HttpStatus.FORBIDDEN, "FAVORITES-003", "해당 즐겨찾기를 삭제할 권한이 없습니다."),
    NOT_EXCEED(HttpStatus.FORBIDDEN, "FAVORITES-004", "최대 저장 공간은 개인당 10개입니다."),
    EQUALS_DESCRIPTION(
            HttpStatus.BAD_REQUEST, "FAVORITES-005",
            "바꾸려는 설명이 이전에 저장된 설명과 같습니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
