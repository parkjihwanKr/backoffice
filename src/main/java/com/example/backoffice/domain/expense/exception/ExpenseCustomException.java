package com.example.backoffice.domain.expense.exception;

import com.example.backoffice.global.exception.CustomException;

public class ExpenseCustomException extends CustomException {
    public ExpenseCustomException(ExpenseExceptionCode e) {
        super(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
