package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberPosition {

    CEO(Position.CEO),
    MANAGER(Position.MANAGER),
    DEPUTY_MANAGER(Position.DEPUTY_MANAGER),
    ASSISTANT_MANAGER(Position.ASSISTANT_MANAGER),
    SENIOR_STAFF(Position.SENIOR_STAFF),
    JUNIOR_STAFF(Position.JUNIOR_STAFF),
    STAFF(Position.STAFF),
    INTERN(Position.INTERN);

    private final String position;

    MemberPosition(String position) {
        this.position = position;
    }

    public static class Position {
        public static final String CEO = "CEO";
        public static final String MANAGER = "MANAGER";
        public static final String DEPUTY_MANAGER = "DEPUTY_MANAGER";
        public static final String ASSISTANT_MANAGER = "ASSISTANT_MANAGER";
        public static final String SENIOR_STAFF = "SENIOR_STAFF";
        public static final String JUNIOR_STAFF = "JUNIOR_STAFF";
        public static final String STAFF = "STAFF";
        public static final String INTERN = "INTERN";
    }
}