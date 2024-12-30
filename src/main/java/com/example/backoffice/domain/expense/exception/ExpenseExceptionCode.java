package com.example.backoffice.domain.expense.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExpenseExceptionCode {
    NOT_FOUND_EXPENSE(HttpStatus.BAD_REQUEST, "EXPENSE-001", "해당 지출을 찾을 수 없습니다."),
    DO_NOT_CREATE_EXPENSE_DEPARTMENT(HttpStatus.BAD_REQUEST, "EXPENSE-002", "해당 부서는 지출 내역서를 만들 수 없습니다"),
    NOT_FOUND_EXPENSE_PROCESS(HttpStatus.BAD_REQUEST,"EXPENSE-003", "잘못된 지출 프로세스 타입입니다."),
    NOT_MATCHED_EXPENSE_DEPARTMENT(HttpStatus.BAD_REQUEST,"EXPENSE-004", "지출 내역서의 부서와 접근하려는 부서가 일치하지 않습니다."),
    RESTRICTED_ACCESS_EXPENSE(HttpStatus.BAD_REQUEST, "EXPENSE-005", "해당 사용자는 접근 권한이 없습니다."),
    DO_NOT_DELETE_EXPENSE_DEPARTMENT(HttpStatus.BAD_REQUEST, "EXPENSE-006", "해당 부서는 지출 내역서를 삭제할 수 없습니다"),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
