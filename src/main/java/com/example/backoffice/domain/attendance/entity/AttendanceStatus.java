package com.example.backoffice.domain.attendance.entity;

import lombok.Getter;

@Getter
public enum AttendanceStatus {
    // 정시 출근, 지각, 결석, 휴일, 조퇴, 외근, 휴가
    ON_TIME(AttendanceStatusLabel.ON_TIME),
    LATE(AttendanceStatusLabel.LATE),
    ABSENT(AttendanceStatusLabel.ABSENT),
    HOLIDAY(AttendanceStatusLabel.HOLIDAY),
    HALF_DAY(AttendanceStatusLabel.HALF_DAY),
    OUT_OF_OFFICE(AttendanceStatusLabel.OUT_OF_OFFICE),
    VACATION(AttendanceStatusLabel.VACATION),
    ;

    private final String label;

    AttendanceStatus(String label){
        this.label = label;
    }

    public static class AttendanceStatusLabel {
        public static final String ON_TIME = "ON_TIME";
        public static final String LATE = "LATE";
        public static final String ABSENT = "ABSENT";
        public static final String HOLIDAY = "HOLIDAY";
        public static final String HALF_DAY = "HALF_DAY";
        public static final String OUT_OF_OFFICE = "OUT_OF_OFFICE";
        public static final String VACATION = "VACATION";
    }
}
