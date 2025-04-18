package com.example.backoffice.domain.event.entity;

import lombok.Getter;

@Getter
public enum EventType {

    COMPANY(EventTypeLabels.COMPANY),
    DEPARTMENT(EventTypeLabels.DEPARTMENT),
    VACATION(EventTypeLabels.VACATION),
    ;

    private final String label;

    EventType(String label) {
        this.label = label;
    }

    public static class EventTypeLabels {
        public static final String COMPANY = "COMPANY";
        public static final String DEPARTMENT = "DEPARTMENT";
        public static final String VACATION = "VACATION";
    }
}
