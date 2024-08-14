package com.example.backoffice.domain.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardsExceptionCode {
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "BOARD-001", "해당 게시글을 찾을 수 없습니다."),
    NOT_MATCHED_ROLE_HOST(HttpStatus.BAD_REQUEST, "BOARD-002", "해당 게시글은 호스트 권한만 게시할 수 있습니다."),
    NOT_MATCHED_MEMBER(HttpStatus.BAD_REQUEST, "BOARD-003", "해당 게시글은 작성자에게 권한이 있습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
