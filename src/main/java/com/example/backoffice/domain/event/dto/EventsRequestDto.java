package com.example.backoffice.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForCompanyEventDto {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForDepartmentEventDto {
        private String title;
        private String description;
        // front-end의 캘린더에서 startDate, endDate를 받는다고 가정
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneForCompanyEventDto {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneForDepartmentEventDto {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
        private String department;
    }
}
