package com.example.backoffice.domain.reaction.exception;

import com.example.backoffice.global.exception.CustomException;

public class ReactionsCustomException extends CustomException {
    public ReactionsCustomException(ReactionsExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
