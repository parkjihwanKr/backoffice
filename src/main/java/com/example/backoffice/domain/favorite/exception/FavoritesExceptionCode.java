package com.example.backoffice.domain.favorite.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FavoritesExceptionCode {
    NOT_FOUND_FAVORITIES(HttpStatus.BAD_REQUEST, "FAVORITIES-001", "해당 즐겨찾기를 찾을 수 없습니다."),
    NO_PERMISSION_TO_READ_FAVORITE(HttpStatus.FORBIDDEN, "FAVORITIES-002", "해당 즐겨찾기를 읽을 권한이 없습니다."),
    INVALID_FAVORITE_TYPE(HttpStatus.BAD_REQUEST, "FAVORITIES-003", "요청하신 즐겨찾기 도메인 타입이 없습니다."),
    NO_PERMISSION_TO_DELETE_FAVORITE(HttpStatus.FORBIDDEN, "FAVORITIES-004", "해당 즐겨찾기를 삭제할 권한이 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
