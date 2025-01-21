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
import com.example.backoffice.domain.vacation.converter.VacationsConverter;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.global.common.DateRange;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventsConverter {

    public static EventDateRangeDto toEventDateRangeDto(
            LocalDateTime startDate, LocalDateTime endDate) {
        return EventDateRangeDto.builder()
                .dateRange(
                        DateRange.builder()
                                .startDate(startDate)
                                .endDate(endDate)
                                .build())
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
                .startDate(eventDateRangeDto.getDateRange().getStartDate())
                .endDate(eventDateRangeDto.getDateRange().getEndDate())
                .department(department)
                .eventType(eventType)
                .build();
    }

    public static EventsResponseDto.CreateOneDepartmentTypeDto toCreateOneForDepartmentDto(Events event){
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for(Files file : event.getFileList()) {
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.CreateOneDepartmentTypeDto.builder()
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

    public static List<EventsResponseDto.ReadOneDepartmentTypeDto> toReadForDepartmentMonthEventDto(
            List<Events> eventList){
        List<EventsResponseDto.ReadOneDepartmentTypeDto> eventResponseDtoList = new ArrayList<>();
        for (Events event : eventList) {
            List<String> fileResponseDtoList = new ArrayList<>();
            for(Files file : event.getFileList()){
                fileResponseDtoList.add(file.getUrl());
            }

            eventResponseDtoList.add(
                    EventsResponseDto.ReadOneDepartmentTypeDto.builder()
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

    public static EventsResponseDto.UpdateOneDepartmentTypeDto toUpdateOneForDepartmentEventDto(
            Events event){
        List<FilesResponseDto.ReadOneDto> fileResponseDtoList = new ArrayList<>();
        for (Files file : event.getFileList()){
            fileResponseDtoList.add(FilesConverter.toReadOneDto(file));
        }
        return EventsResponseDto.UpdateOneDepartmentTypeDto.builder()
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

    public static List<EventsResponseDto.ReadOnePersonalScheduleDto> toReadOneForMemberScheduleDto(
            List<EventsResponseDto.ReadOneAllTypeDto> departmentEventList){
        List<EventsResponseDto.ReadOnePersonalScheduleDto> responseDtoList = new ArrayList<>();
        for (int i =0; i<departmentEventList.size(); i++){
            EventsResponseDto.ReadOneAllTypeDto eventResponseDto = departmentEventList.get(i);
           if(departmentEventList.get(i).getEventType() == EventType.DEPARTMENT){
               responseDtoList.add(
                       EventsResponseDto.ReadOnePersonalScheduleDto.builder()
                               .eventId(eventResponseDto.getEventId())
                               .vacationId(null)
                               .vacationType(null)
                               .isAccepted(null)
                               .department(eventResponseDto.getDepartment())
                               .title(eventResponseDto.getTitle())
                               .description(eventResponseDto.getDescription())
                               .eventType(eventResponseDto.getEventType())
                               .startDate(eventResponseDto.getStartDate())
                               .endDate(eventResponseDto.getEndDate())
                               .createdAt(eventResponseDto.getCreatedAt())
                               .modifiedAt(eventResponseDto.getModifiedAt())
                               .fileUrlList(eventResponseDto.getFileUrlList())
                               .build());
           }else if(departmentEventList.get(i).getEventType() == EventType.VACATION){
               responseDtoList.add(
                       EventsResponseDto.ReadOnePersonalScheduleDto.builder()
                               .eventId(null)
                               .vacationId(eventResponseDto.getVacationId())
                               .department(eventResponseDto.getDepartment())
                               .title(eventResponseDto.getTitle())
                               .description(eventResponseDto.getDescription())
                               .isAccepted(eventResponseDto.getIsAccepted())
                               .eventType(eventResponseDto.getEventType())
                               .vacationType(eventResponseDto.getVacationType())
                               .startDate(eventResponseDto.getStartDate())
                               .endDate(eventResponseDto.getEndDate())
                               .createdAt(eventResponseDto.getCreatedAt())
                               .modifiedAt(eventResponseDto.getModifiedAt())
                               .build());
           }
        }
        return responseDtoList;
    }

    public static List<EventsResponseDto.ReadOneAllTypeDto> toEventResponseDtoListForEvent(List<Events> eventList){
        return eventList.stream().map(
                (event ->
                        EventsResponseDto.ReadOneAllTypeDto.builder()
                                .eventId(event.getId())
                                .title(event.getTitle())
                                .eventType(EventType.DEPARTMENT)
                                .vacationType(null)
                                .isAccepted(null)
                                .vacationId(null)
                                .description(event.getDescription())
                                .startDate(event.getStartDate())
                                .endDate(event.getEndDate())
                                .department(event.getMember().getDepartment())
                                .fileUrlList(
                                        event.getFileList().stream().map(
                                                file -> FilesResponseDto.ReadOneDto.builder()
                                                        .id(file.getId())
                                                        .url(file.getUrl())
                                                        .build()
                                        ).collect(Collectors.toList())
                                )
                                .createdAt(event.getCreatedAt())
                                .modifiedAt(event.getModifiedAt())
                                .build())
        ).collect(Collectors.toList());
    }

    public static List<EventsResponseDto.ReadOneAllTypeDto> toEventResponseDtoListForVacation(
            List<Vacations> vacationList){
        return vacationList.stream().map(
                (vacation ->
                        EventsResponseDto.ReadOneAllTypeDto.builder()
                                .vacationId(vacation.getId())
                                .title(vacation.getTitle())
                                .eventType(EventType.VACATION)
                                .vacationType(vacation.getVacationType())
                                .description(vacation.getUrgentReason())
                                .isAccepted(vacation.getIsAccepted())
                                .startDate(vacation.getStartDate())
                                .endDate(vacation.getEndDate())
                                .department(vacation.getOnVacationMember().getDepartment())
                                .createdAt(vacation.getCreatedAt())
                                .modifiedAt(vacation.getModifiedAt())
                                .build())
        ).collect(Collectors.toList());
    }

    public static List<EventsResponseDto.ReadCompanySummaryOneDto> toReadCompanySummaryListDto(
            List<Events> eventList){
        return eventList.stream()
                .map(EventsConverter::toReadCompanySummaryOneDto)
                .toList();
    }

    public static EventsResponseDto.ReadCompanySummaryOneDto toReadCompanySummaryOneDto(
            Events event){
        return EventsResponseDto.ReadCompanySummaryOneDto.builder()
                .department(event.getDepartment())
                .eventId(event.getId())
                .title(event.getTitle())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}