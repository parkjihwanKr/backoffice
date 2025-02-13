package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberPosition {

    CEO(MemberPositionLabel.CEO),
    MANAGER(MemberPositionLabel.MANAGER),
    DEPUTY_MANAGER(MemberPositionLabel.DEPUTY_MANAGER),
    ASSISTANT_MANAGER(MemberPositionLabel.ASSISTANT_MANAGER),
    SENIOR_STAFF(MemberPositionLabel.SENIOR_STAFF),
    JUNIOR_STAFF(MemberPositionLabel.JUNIOR_STAFF),
    STAFF(MemberPositionLabel.STAFF),
    INTERN(MemberPositionLabel.INTERN);

    private final String position;

    MemberPosition(String position) {
        this.position = position;
    }

    public static class MemberPositionLabel {
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