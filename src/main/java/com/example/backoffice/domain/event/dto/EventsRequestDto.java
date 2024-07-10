package com.example.backoffice.domain.event.dto;

import com.example.backoffice.domain.event.exception.annotation.UrgentReasonRequired;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

public class EventsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneForDepartmentEventDto {
        private String title;
        private String description;
        // front-end의 캘린더에서 startDate, endDate를 받는다고 가정
        // 해당 부분을 String으로 받는다고 함
        private MemberDepartment department;
        private String startDate;
        private String endDate;
        private MultipartFile file;
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
        private MultipartFile file;
        private MemberDepartment department;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @UrgentReasonRequired
    public static class CreateOneForVacationEventDto {
        private String title;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        @NotNull
        private Boolean urgent;
        // urgent를 true로 체크한 사람에 한정해서 사유를 적어야함.
        private String reason;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @UrgentReasonRequired
    public static class UpdateOneForVacationEventDto {
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        @NotNull
        private Boolean urgent;
        private String reason;
    }
}
