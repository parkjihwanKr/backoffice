package com.example.backoffice.global.audit.entity;

import lombok.Getter;

@Getter
public enum AuditLogType {
    LOGIN(eventLabel.LOGIN),
    LOGOUT(eventLabel.LOGOUT),
    SIGNUP(eventLabel.SIGNUP),
    DELETE_MEMBER(eventLabel.DELETE_MEMBER),
    CHANGE_MEMBER_ATTRIBUTE(eventLabel.CHANGE_MEMBER_ATTRIBUTE),
    CHANGE_MEMBER_SALARY(eventLabel.CHANGE_MEMBER_SALARY),
    CHANGE_MEMBER_REMAINING_VACATION_DAY(eventLabel.CHANGE_MEMBER_REMAINING_VACATION_DAY),
    UPLOAD_MEMBER_FILE(eventLabel.UPLOAD_MEMBER_FILE),
    MEMBER_ERROR(eventLabel.MEMBER_ERROR),
    FILE_ERROR(eventLabel.FILE_ERROR),
    CREATE_FILE(eventLabel.CREATE_FILE),
    UPDATE_FILE(eventLabel.UPDATE_FILE),
    DELETE_FILE(eventLabel.DELETE_FILE),
    CREATE_MEMBER_VACATION(eventLabel.CREATE_MEMBER_VACATION),
    UPDATE_MEMBER_VACATION(eventLabel.UPDATE_MEMBER_VACATION),
    CHANGE_BOARD_FILE(eventLabel.CHANGE_BOARD_FILE),
    CHANGE_EVENT(eventLabel.CHANGE_EVENT),
    CHANGE_SECURITY_SETTINGS(eventLabel.CHANGE_SECURITY_SETTINGS);

    private final String label;

    AuditLogType(String label) {
        this.label = label;
    }

    public static class eventLabel {
        public static final String LOGIN = "LOGIN";
        public static final String LOGOUT = "LOGOUT";
        public static final String SIGNUP = "SIGNUP";
        public static final String DELETE_MEMBER = "DELETE_MEMBER";
        public static final String CHANGE_MEMBER_SALARY = "CHANGE_MEMBER_SALARY";
        public static final String CHANGE_MEMBER_ATTRIBUTE = "CHANGE_MEMBER_ATTRIBUTE";
        public static final String CHANGE_MEMBER_REMAINING_VACATION_DAY = "CHANGE_REMAINING_VACATION_DAY";
        public static final String UPLOAD_MEMBER_FILE = "CHANGE_MEMBER_FILE";
        public static final String MEMBER_ERROR = "MEMBER_ERROR";
        public static final String FILE_ERROR = "FILE_ERROR";
        public static final String CREATE_FILE = "CREATE_FILE";
        public static final String UPDATE_FILE = "UPDATE_FILE";
        public static final String DELETE_FILE = "DELETE_FILE";
        public static final String CREATE_MEMBER_VACATION = "CREATE_MEMBER_VACATION";
        public static final String UPDATE_MEMBER_VACATION = "UPDATE_MEMBER_VACATION";
        public static final String CHANGE_BOARD_FILE = "CHANGE_BOARD_FILE";
        public static final String CHANGE_EVENT = "CHANGE_EVENT";
        public static final String CHANGE_SECURITY_SETTINGS = "CHANGE_SECURITY_SETTINGS";
    }
}
