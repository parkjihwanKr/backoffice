package com.example.backoffice.domain.board.exception;

import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.global.exception.CustomException;

public class BoardsCustomException extends CustomException {
    public BoardsCustomException(BoardsExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
