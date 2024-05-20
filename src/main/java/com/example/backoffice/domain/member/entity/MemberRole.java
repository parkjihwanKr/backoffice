package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberRole {

    // 메인 어드민
    ADMIN(Authority.ADMIN),
    HR(Authority.HR),
    FINANCE(Authority.FINANCE),
    IT(Authority.IT),
    MARKETING(Authority.MARKETING),
    SALES(Authority.SALES);

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String FINANCE = "ROLE_FINANCE";
        public static final String IT = "ROLE_IT";
        public static final String SALES = "ROLE_SALES";
        public static final String MARKETING = "ROLE_MARKETING";
        public static final String HR = "ROLE_HR";
    }
}