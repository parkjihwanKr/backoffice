package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberRole {

    // 메인 어드민
    USER(Authority.USER),
    ADMIN(Authority.ADMIN),
    MAIN_ADMIN(Authority.MAIN_ADMIN);

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String MAIN_ADMIN = "ROLE_MAIN_ADMIN";
        public static final String USER = "ROLE_USER";
    }
}