package com.example.backoffice.domain.finance.exception;

import com.example.backoffice.global.exception.CustomException;

public class FinanceCustomException extends CustomException {
    public FinanceCustomException(FinanceExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
