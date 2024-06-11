package com.example.backoffice.domain.notification.entity;

import lombok.Getter;
import org.springframework.security.core.parameters.P;

@Getter
public enum NotificationType {
    MEMBER(Domain.MEMBER),
    BOARD(Domain.BOARD),
    COMMENT(Domain.COMMENT),
    REPLY(Domain.REPLY),
    EVENT(Domain.EVENT),
    URGENT_VACATION_EVENT(Domain.URGENT_VACATION_EVENT),
    URGENT_SERVER_ISSUE(Domain.URGENT_SERVER_ISSUE),
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
        public static final String URGENT_VACATION_EVENT = "VACATION EVENT";
        public static final String URGENT_SERVER_ISSUE = "URGENT_SERVER_ISSUE";
    }
}
