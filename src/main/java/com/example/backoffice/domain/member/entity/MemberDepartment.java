package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberDepartment {
    HR(Department.HR),
    FINANCE(Department.FINANCE),
    IT(Department.IT),
    MARKETING(Department.MARKETING),
    SALES(Department.SALES);

    private final String department;

    MemberDepartment(String department) {
        this.department = department;
    }

    public static class Department {
        public static final String FINANCE = "ROLE_FINANCE";
        public static final String IT = "ROLE_IT";
        public static final String SALES = "ROLE_SALES";
        public static final String MARKETING = "ROLE_MARKETING";
        public static final String HR = "ROLE_HR";
    }
}
