package com.example.backoffice.domain.event.entity;

import lombok.Getter;

@Getter
public enum EventCrudType {
    CREATE_DEPARTMENT(eventLabelTypes.CREATE_DEPARTMENT),
    UPDATE_DEPARTMENT(eventLabelTypes.UPDATE_DEPARTMENT),
    CREATE_VACATION(eventLabelTypes.CREATE_VACATION),
    UPDATE_VACATION(eventLabelTypes.UPDATE_VACATION),
    DELETE_VACATION(eventLabelTypes.DELETE_VACATION),
    ;

    private final String label;

    EventCrudType(String label) {
        this.label = label;
    }

    public static class eventLabelTypes {
        public static final String CREATE_DEPARTMENT = "부서 이벤트 생성";
        public static final String UPDATE_DEPARTMENT = "부서 이벤트 수정";
        public static final String CREATE_VACATION = "휴가 이벤트 생성";
        public static final String UPDATE_VACATION = "휴가 이벤트 수정";
        public static final String DELETE_VACATION = "휴가 이벤트 삭제";
    }
}
