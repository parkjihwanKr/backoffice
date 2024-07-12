package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberDepartment {
    HR(Department.HR),
    FINANCE(Department.FINANCE),
    IT(Department.IT),
    MARKETING(Department.MARKETING),
    SALES(Department.SALES),
    AUDIT(Department.AUDIT);

    private final String department;

    MemberDepartment(String department) {
        this.department = department;
    }

    public static class Department {
        public static final String FINANCE = "FINANCE";
        public static final String IT = "IT";
        public static final String SALES = "SALES";
        public static final String MARKETING = "MARKETING";
        public static final String HR = "HR";
        public static final String AUDIT = "AUDIT";
    }
}
