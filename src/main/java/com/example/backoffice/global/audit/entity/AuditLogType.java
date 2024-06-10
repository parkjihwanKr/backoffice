package com.example.backoffice.global.audit.entity;

import lombok.Getter;

@Getter
public enum AuditLogType {
    LOGIN(eventLabel.LOGIN),
    LOGOUT(eventLabel.LOGOUT),
    SIGNUP(eventLabel.SIGNUP),
    PASSWORD_CHANGE(eventLabel.PASSWORD_CHANGE),
    DEPARTMENT_CHANGE(eventLabel.DEPARTMENT_CHANGE),
    POSITION_CHANGE(eventLabel.POSITION_CHANGE),
    SALARY_CHANGE(eventLabel.SALARY_CHANGE),
    REMAINING_VACATION_DAY_CHANGE(eventLabel.REMAINING_VACATION_DAY_CHANGE),
    FILE_CHANGE(eventLabel.FILE_CHANGE),
    EVENT_CHANGE(eventLabel.EVENT_CHANGE),
    SECURITY_SETTINGS_CHANGE(eventLabel.SECURITY_SETTINGS_CHANGE)
    ;

    private final String label;

    AuditLogType(String label){
        this.label = label;
    }

    public static class eventLabel{
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
        public static final String SIGNUP = "SIGNUP";
        public static final String PASSWORD_CHANGE = "PASSWORD_CHANGE";
        public static final String DEPARTMENT_CHANGE = "DEPARTMENT_CHANGE";
        public static final String POSITION_CHANGE = "POSITION_CHANGE";
        public static final String SALARY_CHANGE = "SALARY_CHANGE";
        public static final String REMAINING_VACATION_DAY_CHANGE = "REMAINING_VACATION_DAY_CHANGE";
        public static final String FILE_CHANGE = "FILE_CHANGE";
        public static final String EVENT_CHANGE = "EVENT_CHANGE";
        public static final String SECURITY_SETTINGS_CHANGE = "SECURITY_SETTINGS_CHANGE";
    }
}
