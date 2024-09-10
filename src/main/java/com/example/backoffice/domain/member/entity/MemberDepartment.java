package com.example.backoffice.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberDepartment {
    HR(MemberDepartmentLabel.HR),
    FINANCE(MemberDepartmentLabel.FINANCE),
    IT(MemberDepartmentLabel.IT),
    MARKETING(MemberDepartmentLabel.MARKETING),
    SALES(MemberDepartmentLabel.SALES),
    AUDIT(MemberDepartmentLabel.AUDIT);

    private final String department;

    MemberDepartment(String department) {
        this.department = department;
    }

    public static class MemberDepartmentLabel {
        public static final String FINANCE = "FINANCE";
        public static final String IT = "IT";
        public static final String SALES = "SALES";
        public static final String MARKETING = "MARKETING";
        public static final String HR = "HR";
        public static final String AUDIT = "AUDIT";
    }
}
