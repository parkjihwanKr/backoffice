package com.example.backoffice.domain.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsRequestDto.CreateOneDepartmentTypeDto",
            description = "부서 타입 일정 하나 생성 요청 DTO")
    public static class CreateOneDepartmentTypeDto {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsRequestDto.UpdateOneDepartmentTypeDto",
            description = "부서 타입 일정 하나 생성 요청 DTO")
    public static class UpdateOneDepartmentTypeDto {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
        private String department;
    }
}
