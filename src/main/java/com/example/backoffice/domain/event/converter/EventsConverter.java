package com.example.backoffice.domain.event.converter;

import com.example.backoffice.domain.event.dto.EventDateRangeDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.EventType;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.file.converter.FilesConverter;
import com.example.backoffice.domain.file.dto.FilesResponseDto;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class EventsConverter {

    public static EventDateRangeDto toEventDateRangeDto(
            LocalDateTime startDate, LocalDateTime endDate) {
        return EventDateRangeDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static Events toEntity(
            String title, String description,
            EventDateRangeDto eventDateRangeDto, Members member,
            MemberDepartment department, EventType eventType){
        return Events.builder()
                .title(title)
                .description(description)
                .member(member)
                .startDate(eventDateRangeDto.getStartDate())
                .endDate(eventDateRangeDto.getEndDate())
                .department(department)
                .eventType(eventType)
                .build();
    }

    public static EventsResponseDto.CreateOneForCompanyEventDto toCreateOneForCompanyEventDto(
            Events event){
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for(Files file : event.getFileList()) {
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.CreateOneForCompanyEventDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .fileUrlList(fileResponseDtoList)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .build();
    }

    public static EventsResponseDto.ReadOneForCompanyEventDto toReadOneForCompanyEventDto(
            Events event) {
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for(Files file : event.getFileList()) {
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.ReadOneForCompanyEventDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .fileUrlList(fileResponseDtoList)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }

    public static List<EventsResponseDto.ReadOneForCompanyEventDto> toReadForCompanyMonthEventDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadOneForCompanyEventDto> eventResponseDtoList = new ArrayList<>();
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for (Events event : eventList) {
            for(Files file : event.getFileList()) {
                fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
            }
            eventResponseDtoList.add(
                    EventsResponseDto.ReadOneForCompanyEventDto.builder()
                            .eventId(event.getId())
                            .title(event.getTitle())
                            .fileUrlList(fileResponseDtoList)
                            .startDate(event.getStartDate())
                            .endDate(event.getEndDate())
                            .createdAt(event.getCreatedAt())
                            .build()
            );
        }
        return eventResponseDtoList;
    }

    public static List<List<EventsResponseDto.ReadOneForCompanyEventDto>> toReadForCompanyYearEventDto(
            List<Events> eventList) {
        List<List<EventsResponseDto.ReadOneForCompanyEventDto>> yearEvents = new ArrayList<>();

        for (Month month : Month.values()) {
            List<EventsResponseDto.ReadOneForCompanyEventDto> monthlyEvents = new ArrayList<>();
            List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();

            for (Events event : eventList) {
                for(Files file : event.getFileList()) {
                    fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
                }

                if (event.getStartDate().getMonth().equals(month)) {
                    monthlyEvents.add(
                            EventsResponseDto.ReadOneForCompanyEventDto.builder()
                                    .eventId(event.getId())
                                    .title(event.getTitle())
                                    .description(event.getDescription())
                                    .fileUrlList(fileResponseDtoList)
                                    .startDate(event.getStartDate())
                                    .endDate(event.getEndDate())
                                    .createdAt(event.getCreatedAt())
                                    .modifiedAt(event.getModifiedAt())
                                    .build()
                    );
                }
            }
            yearEvents.add(monthlyEvents);
        }
        return yearEvents;
    }

    public static EventsResponseDto.UpdateOneForCompanyEventDto toUpdateOneForCompanyEventDto(
            Events event){
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for (Files file : event.getFileList()){
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.UpdateOneForCompanyEventDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .department(event.getDepartment())
                .fileUrlList(fileResponseDtoList)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }

    public static EventsResponseDto.CreateOneForDepartmentEventDto toCreateOneForDepartmentDto(Events event){
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for(Files file : event.getFileList()) {
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.CreateOneForDepartmentEventDto.builder()
            .eventId(event.getId())
            .title(event.getTitle())
            .description(event.getDescription())
            .department(event.getDepartment())
            .fileUrlList(fileResponseDtoList)
            .startDate(event.getStartDate())
            .endDate(event.getEndDate())
            .createdAt(event.getCreatedAt())
            .modifiedAt(event.getModifiedAt())
            .build();
    }

    public static List<EventsResponseDto.ReadOneForDepartmentEventDto> toReadForDepartmentMonthEventDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadOneForDepartmentEventDto> eventResponseDtoList = new ArrayList<>();
        for (Events event : eventList) {
            List<String> fileResponseDtoList = new ArrayList<>();
            for(Files file : event.getFileList()){
                fileResponseDtoList.add(file.getUrl());
            }

            eventResponseDtoList.add(
                    EventsResponseDto.ReadOneForDepartmentEventDto.builder()
                            .eventId(event.getId())
                            .title(event.getTitle())
                            .description(event.getDescription())
                            .department(event.getDepartment())
                            .fileUrlList(fileResponseDtoList)
                            .startDate(event.getStartDate())
                            .endDate(event.getEndDate())
                            .createdAt(event.getCreatedAt())
                            .modifiedAt(event.getModifiedAt())
                            .build()
            );
        }
        return eventResponseDtoList;
    }

    public static EventsResponseDto.UpdateOneForDepartmentEventDto toUpdateOneForDepartmentEventDto(
            Events event){
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for (Files file : event.getFileList()){
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.UpdateOneForDepartmentEventDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .department(event.getDepartment())
                .fileUrlList(fileResponseDtoList)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }

    public static EventsResponseDto.CreateOneForVacationEventDto toCreateOneForVacationEventDto(
            Events event, Boolean urgent){
        return EventsResponseDto.CreateOneForVacationEventDto.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .urgent(urgent)
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }

    public static List<EventsResponseDto.ReadOneForMemberScheduleDto> toReadForMemberScheduleDto(
            List<Events> filteredEventList){
        List<EventsResponseDto.ReadOneForMemberScheduleDto> memberMonthScheduleResponseDto = new ArrayList<>();
        for (Events filteredEvent : filteredEventList) {
            memberMonthScheduleResponseDto.add(
                    EventsResponseDto.ReadOneForMemberScheduleDto.builder()
                            .eventId(filteredEvent.getId())
                            .title(filteredEvent.getTitle())
                            .description(filteredEvent.getDescription())
                            .department(filteredEvent.getDepartment())
                            .startDate(filteredEvent.getStartDate())
                            .endDate(filteredEvent.getEndDate())
                            .createdAt(filteredEvent.getCreatedAt())
                            .modifiedAt(filteredEvent.getModifiedAt())
                            .build());
        }
        return memberMonthScheduleResponseDto;
    }

    public static List<EventsResponseDto.ReadOneForVacationEventDto> toReadForVacationMonthEventDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadOneForVacationEventDto> responseDtoList = new ArrayList<>();
        for(Events event : eventList){
            responseDtoList.add(
                    EventsResponseDto.ReadOneForVacationEventDto.builder()
                            .eventId(event.getId())
                            .vacationMemberName(event.getMember().getMemberName())
                            .startDate(event.getStartDate())
                            .endDate(event.getEndDate())
                            .build()
            );
        }
        return responseDtoList;
    }

    public static EventsResponseDto.UpdateOneForVacationEventDto toUpdateOneForVacationEventDto(
            Events vacation, String memberName){
        return EventsResponseDto.UpdateOneForVacationEventDto.builder()
                .eventId(vacation.getId())
                .vacationMemberName(memberName)
                .startDate(vacation.getStartDate())
                .endDate(vacation.getEndDate())
                .build();
    }

    public static List<EventsResponseDto.ReadMemberForVacationEventDto> toReadMemberListForVacationEventDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadMemberForVacationEventDto> responseDtoList = new ArrayList<>();
        for(Events event : eventList){
            responseDtoList.add(
                    EventsResponseDto.ReadMemberForVacationEventDto.builder()
                            .eventId(event.getId())
                            .vacationMemberName(event.getMember().getMemberName())
                            .startDate(event.getStartDate())
                            .endDate(event.getEndDate())
                            .build()
            );
        }
        return responseDtoList;
    }
}
