package com.example.backoffice.domain.vacation.entity;

import lombok.Getter;

@Getter
public enum VacationType {
    ANNUAL_LEAVE(VacationTypeLabels.ANNUAL_LEAVE),
    SICK_LEAVE(VacationTypeLabels.SICK_LEAVE),
    URGENT_LEAVE(VacationTypeLabels.URGENT_LEAVE);

    private final String label;

    VacationType(String label){
        this.label = label;
    }

    public static class VacationTypeLabels {
        public static final String ANNUAL_LEAVE = "연가";
        public static final String SICK_LEAVE = "병가";
        public static final String URGENT_LEAVE = "긴급한 휴가";
    }
}
