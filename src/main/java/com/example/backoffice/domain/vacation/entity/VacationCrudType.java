package com.example.backoffice.domain.vacation.entity;

import lombok.Getter;

@Getter
public enum VacationCrudType {
    READ_VACATION(vacationLabelTypes.READ_VACATION),
    CREATE_VACATION(vacationLabelTypes.CREATE_VACATION),
    UPDATE_VACATION(vacationLabelTypes.UPDATE_VACATION),
    DELETE_VACATION(vacationLabelTypes.DELETE_VACATION),
    ;

    private final String label;

    VacationCrudType(String label) {
        this.label = label;
    }

    public static class vacationLabelTypes {
        public static final String READ_VACATION = "휴가 이벤트 조회";
        public static final String CREATE_VACATION = "휴가 이벤트 생성";
        public static final String UPDATE_VACATION = "휴가 이벤트 수정";
        public static final String DELETE_VACATION = "휴가 이벤트 삭제";
    }
}
