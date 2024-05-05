package com.example.backoffice.domain.comment.exception;

import com.example.backoffice.global.exception.CustomException;

public class CommentsCustomException extends CustomException {
    public CommentsCustomException(CommentsExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
