package com.example.backoffice.domain.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    MEMBER(Domain.MEMBER),
    BOARD(Domain.BOARD),
    COMMENT(Domain.COMMENT),
    REPLY(Domain.REPLY)
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
    }
}
