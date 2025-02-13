package com.example.backoffice.domain.event.dto;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.entity.VacationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class EventsResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsResponseDto.CreateOneDepartmentTypeDto",
            description = "부서 타입 일정 하나 생성 요청 DTO")
    public static class CreateOneDepartmentTypeDto {
        private Long eventId;
        private String title;
        private String description;
        private MemberDepartment department;
        private List<FilesResponseDto.ReadOneDto> fileUrlList;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "EventsResponseDto.ReadOneDepartmentTypeDto",
            description = "부서 타입 일정 하나 조회 요청 DTO")
    public static class ReadOneDepartmentTypeDto {
        private Long eventId;
        private String title;
        private String description;
        private MemberDepartment department;
        private List<String> fileUrlList;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsResponseDto.UpdateOneDepartmentTypeDto",
            description = "부서 타입 일정 하나 수정 요청 DTO")
    public static class UpdateOneDepartmentTypeDto {
        private Long eventId;
        private String title;
        private String description;
        private MemberDepartment department;
        private List<FilesResponseDto.ReadOneDto> fileUrlList;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsResponseDto.ReadOnePersonalScheduleDto",
            description = "개인 일정 하나 조회 요청 DTO")
    public static class ReadOnePersonalScheduleDto {
        private Long eventId;
        private Long vacationId;
        private String title;
        private String description;
        private Boolean isAccepted;
        private EventType eventType;
        private MemberDepartment department;
        private VacationType vacationType;
        private List<FilesResponseDto.ReadOneDto> fileUrlList;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsResponseDto.ReadOneAllTypeDto",
            description = "개인과 부서을 통합한 일정 하나 조회 요청 DTO")
    public static class ReadOneAllTypeDto{
        private Long eventId;
        private Long vacationId;
        private String title;
        private String description;
        private Boolean isAccepted;
        private EventType eventType;
        private VacationType vacationType;
        private MemberDepartment department;
        private List<FilesResponseDto.ReadOneDto> fileUrlList;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "EventsResponseDto.ReadCompanySummaryOneDto",
            description = "요약된 회사 일정 조회 요청 DTO")
    public static class ReadCompanySummaryOneDto {
        private Long eventId;
        private String title;
        private EventType eventType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private MemberDepartment department;
    }
}
