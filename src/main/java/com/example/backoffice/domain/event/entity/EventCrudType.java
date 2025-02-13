package com.example.backoffice.domain.event.entity;

import lombok.Getter;

@Getter
public enum EventCrudType {
    CREATE_DEPARTMENT(eventLabelTypes.CREATE_DEPARTMENT),
    UPDATE_DEPARTMENT(eventLabelTypes.UPDATE_DEPARTMENT),
    ;

    private final String label;

    EventCrudType(String label) {
        this.label = label;
    }

    public static class eventLabelTypes {
        public static final String CREATE_DEPARTMENT = "부서 이벤트 생성";
        public static final String UPDATE_DEPARTMENT = "부서 이벤트 수정";
    }
}
