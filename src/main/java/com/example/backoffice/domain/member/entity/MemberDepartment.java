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
        public static final String FINANCE = "FINANCE_DEPARTMENT";
        public static final String IT = "IT_DEPARTMENT";
        public static final String SALES = "SALES_DEPARTMENT";
        public static final String MARKETING = "MARKETING_DEPARTMENT";
        public static final String HR = "HR_DEPARTMENT";
        public static final String AUDIT = "AUDIT_DEPARTMENT";
    }
}
