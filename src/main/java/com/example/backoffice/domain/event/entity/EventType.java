package com.example.backoffice.domain.event.entity;

import lombok.Getter;

@Getter
public enum EventType {
    COMPANY(EventTypeLabels.COMPANY),
    DEPARTMENT(EventTypeLabels.DEPARTMENT),
    MEMBER_VACATION(EventTypeLabels.MEMBER_VACATION);

    private final String label;

    EventType(String label) {
        this.label = label;
    }

    public static class EventTypeLabels {
        public static final String COMPANY = "COMPANY_EVENT";
        public static final String DEPARTMENT = "DEPARTMENT_EVENT";
        public static final String MEMBER_VACATION = "MEMBER_VACATION";
    }
}
