package com.example.backoffice.domain.vacation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VacationsRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "VacationsRequestDto.UpdatePeriodDto",
            description = "휴가 신청 기간 요청 DTO")
    public static class UpdatePeriodDto {
        private String startDate;
        private String endDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "VacationsRequestDto.CreateOneDto",
            description = "멤버 휴가 생성 요청 DTO")
    public static class CreateOneDto {
        private String title;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        @NotNull
        private Boolean urgent;
        private String urgentReason;
        private String vacationType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "VacationsRequestDto.UpdateOneDto",
            description = "멤버 휴가 수정 요청 DTO")
    public static class UpdateOneDto {
        private String title;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;
        private Boolean urgent;
        private String urgentReason;
        private String vacationType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "VacationsRequestDto.DeleteOneByAdminDto",
            description = "관리자에 의한 멤버의 휴가 삭제 요청 DTO")
    public static class DeleteOneByAdminDto {
        private String reason;
    }
}
