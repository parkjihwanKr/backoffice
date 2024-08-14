package com.example.backoffice.domain.evaluation.entity;

import lombok.Getter;

@Getter
public enum EvaluationType {
    COMPANY(EvaluationLabelType.COMPANY),
    DEPARTMENT(EvaluationLabelType.DEPARTMENT);

    private final String label;

    EvaluationType(String label){
        this.label = label;
    }

    public static class EvaluationLabelType{
        public static String COMPANY = "EVALUATIONS_COMPANY";
        public static String DEPARTMENT = "EVALUAIONS_DEPARTMENT";
    }
}
