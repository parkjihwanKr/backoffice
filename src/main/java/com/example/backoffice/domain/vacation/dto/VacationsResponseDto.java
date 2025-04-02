package com.example.backoffice.domain.vacation.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.entity.VacationType;
import com.example.backoffice.global.redis.deserializer.VacationPeriodDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VacationsResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "VacationsResponseDto.UpdatePeriodDto",
            description = "휴가 신청 기간 수정 응답 DTO")
    public static class UpdatePeriodDto {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonDeserialize(using = VacationPeriodDeserializer.class)
    @Schema(name = "VacationsResponseDto.ReadPeriodDto",
            description = "휴가 신청 기간 조회 응답 DTO")
    public static class ReadPeriodDto {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "VacationsResponseDto.CreateOneDto",
            description = "멤버 휴가 생성 응답 DTO")
    public static class CreateOneDto {
        private String title;
        private VacationType vacationType;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "VacationsResponseDto.ReadDayDto",
            description = "멤버 휴가 특정일 조회 응답 DTO")
    public static class ReadDayDto {
        private Long vacationId;
        private String onVacationMemberName;
        private String title;
        private Boolean isAccepted;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private VacationType vacationType;
        private Boolean urgent;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "VacationsResponseDto.ReadMonthDto",
            description = "멤버 휴가 특정달 조회 응답 DTO")
    public static class ReadMonthDto {
        private Long vacationId;
        private String onVacationMemberName;
        private String title;
        private Boolean urgent;
        private String urgentReason;
        private Boolean isAccepted;
        private MemberDepartment department;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private VacationType vacationType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "VacationsResponseDto.UpdateOneDto",
            description = "멤버 휴가 수정 응답 DTO")
    public static class UpdateOneDto {
        private Long vacationId;
        private String title;
        private String urgentReason;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private VacationType vacationType;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneByAdminDto {
        private Long vacationId;
        private String acceptedVacationMemberName;
        private Boolean isAccepted;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneIsAcceptedDto {
        private Long vacationId;
        private String onVacationMemberName;
        private Boolean isAccepted;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "VacationsResponseDto.ReadSummaryOneDto",
            description = "요약된 멤버 휴가 하나 응답 DTO")
    public static class ReadSummaryOneDto{
        private Long vacationId;
        private String onVacationMemberName;
        private VacationType vacationType;
        private Boolean isAccepted;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String urgentReason;
    }
}
