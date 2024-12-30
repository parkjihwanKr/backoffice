package com.example.backoffice.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentsExceptionCode {
    NOT_FOUND_COMMENT(
            HttpStatus.BAD_REQUEST, "COMMENT-001",
            "해당 댓글을 찾을 수 없습니다."),
    NOT_MATCHED_BOARD_COMMENT(
            HttpStatus.BAD_REQUEST, "COMMENT-002",
            "게시글에서 해당 댓글을 찾을 수 없습니다."),
    NOT_MATCHED_MEMBER_COMMENT(
            HttpStatus.BAD_REQUEST, "COMMENT-003",
            "해당 멤버는 게시글의 작성자가 아닙니다."),
    NOT_MATCHED_COMMENT_REPLY(
            HttpStatus.BAD_REQUEST, "COMMENT-004",
            "댓글에 해당하는 대댓글이 아닙니다."),
    IS_COMMENT(
            HttpStatus.BAD_REQUEST, "COMMENT-005",
            "바꾸려는 글은 댓글입니다."),
    IS_EMPTY_COMMENT_CONTENT(
            HttpStatus.BAD_REQUEST, "COMMENT-006",
            "댓글 또는 대댓글 내용을 입력해주세요."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
