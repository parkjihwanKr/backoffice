package com.example.backoffice.domain.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum BoardsExceptionCode {
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "BOARD-001", "해당 게시글을 찾을 수 없습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
