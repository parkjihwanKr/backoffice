package com.example.backoffice.domain.member.exception;

import com.example.backoffice.global.exception.CustomException;

public class MembersCustomException extends CustomException {
    public MembersCustomException(MembersExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
