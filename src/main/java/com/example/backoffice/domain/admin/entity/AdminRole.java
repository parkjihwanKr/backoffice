package com.example.backoffice.domain.admin.entity;

import lombok.Getter;

@Getter
public enum AdminRole {

    MAIN_ADMIN(AdminRole.Authority.MAIN_ADMIN),
    HR(AdminRole.Authority.HR),
    FINANCE(AdminRole.Authority.FINANCE),
    IT(AdminRole.Authority.IT),
    MARKETING(AdminRole.Authority.MARKETING),
    SALES(AdminRole.Authority.SALES);

    private final String authority;

    AdminRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String MAIN_ADMIN = "ROLE_MAIN_ADMIN";
        public static final String HR = "ROLE_ADMIN";
        public static final String IT = "ROLE_IT";
        public static final String FINANCE = "ROLE_FINANCE";
        public static final String MARKETING = "ROLE_MARKETING";
        public static final String SALES = "ROLE_SALES";
    }
}
