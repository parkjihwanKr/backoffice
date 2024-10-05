package com.example.backoffice.domain.event.dto;

import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
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
    public static class CreateOneForCompanyEventDto {
        private Long eventId;
        private String title;
        private String description;
        private List<FilesResponseDto.ReadOneDto> fileUrlList;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadOneForCompanyEventDto {
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
    public static class CreateOneForDepartmentEventDto {
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
    public static class UpdateOneForCompanyEventDto {
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
    public static class ReadOneForDepartmentEventDto {
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
    public static class UpdateOneForDepartmentEventDto {
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
    public static class ReadOneForMemberScheduleDto {
        private Long eventId;
        private Long vacationId;
        private String title;
        private String description;
        private EventType eventType;
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
    public static class ReadOneForEventDto{
        private Long eventId;
        private Long vacationId;
        private String title;
        private String description;
        private EventType eventType;
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
    public static class ReadOneForVacationDto{
        private Long vacationId;
        private String title;
        private String description;
        private EventType eventType;
        private MemberDepartment department;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
