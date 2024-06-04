package com.example.backoffice.domain.event.entity;

import lombok.Getter;

@Getter
public enum EventCrudType {
    CREATE(eventLabelTypes.CREATE),
    UPDATE(eventLabelTypes.UPDATE);

    private final String label;

    EventCrudType(String label){
        this.label = label;
    }

    public static class eventLabelTypes{
        public static final String CREATE = "이벤트 생성";
        public static final String UPDATE = "이벤트 수정";
    }
}
