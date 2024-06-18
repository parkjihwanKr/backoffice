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
        public static final String CEO = "POSITION_CEO";
        public static final String MANAGER = "POSITION_MANAGER";
        public static final String DEPUTY_MANAGER = "POSITION_DEPUTY_MANAGER";
        public static final String ASSISTANT_MANAGER = "POSITION_ASSISTANT_MANAGER";
        public static final String SENIOR_STAFF = "POSITION_SENIOR_STAFF";
        public static final String JUNIOR_STAFF = "POSITION_JUNIOR_STAFF";
        public static final String STAFF = "POSITION_STAFF";
        public static final String INTERN = "POSITION_INTERN";
    }
}