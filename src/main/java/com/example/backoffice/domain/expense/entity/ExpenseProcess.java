package com.example.backoffice.domain.expense.entity;

import lombok.Getter;

@Getter
public enum ExpenseProcess {
    APPROVED(ExpenseProcessLabel.APPROVED),
    REJECTED(ExpenseProcessLabel.REJECTED),
    PENDING(ExpenseProcessLabel.PENDING),
    ;

    private String process;

    ExpenseProcess(String process) {
        this.process = process;
    }

    public static class ExpenseProcessLabel {
        public static final String APPROVED = "승인";
        public static final String REJECTED = "거절";
        public static final String PENDING = "검토중";
    }
}
