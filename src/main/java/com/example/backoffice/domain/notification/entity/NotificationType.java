package com.example.backoffice.domain.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    MEMBER(Domain.MEMBER),
    BOARD(Domain.BOARD),
    COMMENT(Domain.COMMENT),
    REPLY(Domain.REPLY),
    EVENT(Domain.EVENT),
    URGENT_VACATION(Domain.URGENT_VACATION),
    URGENT_SERVER_ERROR(Domain.URGENT_SERVER_ERROR),
    EVALUATION(Domain.EVALUATION),
    UPDATE_EVALUATION(Domain.UPDATE_EVALUATION),
    UPDATE_VACATION_PERIOD(Domain.UPDATE_VACATION_PERIOD),
    IS_ACCEPTED_VACATION(Domain.IS_ACCEPTED_VACATION),
    DELETE_VACATION_FOR_ADMIN(Domain.DELETE_VACATION_FOR_ADMIN)
    ;

    private final String domain;

    NotificationType(String domain) {
        this.domain = domain;
    }

    public static class Domain {

        public static final String MEMBER = "MEMBER";
        public static final String BOARD = "BOARD";
        public static final String COMMENT = "COMMENT";
        public static final String REPLY = "REPLY";
        public static final String EVENT = "EVENT";
        public static final String URGENT_VACATION = "URGENT_VACATION";
        public static final String URGENT_SERVER_ERROR = "URGENT_SERVER_ERROR";
        public static final String EVALUATION = "EVALUATION";
        public static final String UPDATE_EVALUATION = "UPDATE_EVALUATION";
        public static final String UPDATE_VACATION_PERIOD = "UPDATE_VACATION_PERIOD";
        public static final String IS_ACCEPTED_VACATION = "IS_ACCEPTED_VACATION";
        public static final String DELETE_VACATION_FOR_ADMIN = "DELETE_VACATION_FOR_ADMIN";
    }
}
