package com.example.backoffice.domain.board.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardsExceptionCode {
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "BOARD-001", "해당 게시글을 찾을 수 없습니다."),
    NOT_MATCHED_ROLE_HOST(HttpStatus.BAD_REQUEST, "BOARD-002", "해당 게시글은 호스트 권한만 게시할 수 있습니다."),
    NOT_MATCHED_BOARD_OWNER(HttpStatus.BAD_REQUEST, "BOARD-003", "해당 게시글은 작성자에게 권한이 있습니다."),
    UNAUTHORIZED_DEPARTMENT_BOARD_CREATION(HttpStatus.BAD_REQUEST, "BOARD-004", "해당 멤버는 부서 게시글을 만들 권한이 없습니다."),
    NOT_DEPARTMENT_BOARD(HttpStatus.BAD_REQUEST, "BOARD-005","요청하신 게시판이 부서 타입의 게시판이 아닙니다."),
    NOT_GENERAL_BOARD(HttpStatus.BAD_REQUEST, "BOARD-006", "요청하신 게시판이 전체 게시판이 아닙니다."),
    NOT_FOUND_BOARD_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "BOARD-007", "요청하신 게시판 타입이 존재하지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "BOARD-008", "접근할 수 없는 부서 게시판입니다."),
    NOT_FOUND_BOARD_CATEGORIES(HttpStatus.BAD_REQUEST, "BOARD-009", "해당 게시글의 카테고리를 찾을 수 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
