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

    public static List<EventsResponseDto.ReadOneForMemberScheduleDto> toReadOneForMemberScheduleDto(
            List<EventsResponseDto.ReadOneForEventDto> departmentEventList){
        List<EventsResponseDto.ReadOneForMemberScheduleDto> responseDtoList = new ArrayList<>();
        for (int i =0; i<departmentEventList.size(); i++){
            EventsResponseDto.ReadOneForEventDto eventResponseDto = departmentEventList.get(i);
           if(departmentEventList.get(i).getEventType() == EventType.DEPARTMENT){
               responseDtoList.add(
                       EventsResponseDto.ReadOneForMemberScheduleDto.builder()
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
                       EventsResponseDto.ReadOneForMemberScheduleDto.builder()
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

    public static List<EventsResponseDto.ReadOneForEventDto> toEventResponseDtoListForEvent(List<Events> eventList){
        return eventList.stream().map(
                (event ->
                        EventsResponseDto.ReadOneForEventDto.builder()
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

    public static List<EventsResponseDto.ReadOneForEventDto> toEventResponseDtoListForVacation(
            List<Vacations> vacationList){
        return vacationList.stream().map(
                (vacation ->
                        EventsResponseDto.ReadOneForEventDto.builder()
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

    public static List<EventsResponseDto.ReadDepartmentSummaryDto> toReadDepartmentSummaryListDto(
            List<Events> eventList){
        return eventList.stream()
                .map(EventsConverter::toReadDepartmentSummaryDto)
                .toList();
    }

    public static EventsResponseDto.ReadDepartmentSummaryDto toReadDepartmentSummaryDto(
            Events event){
        return EventsResponseDto.ReadDepartmentSummaryDto.builder()
                .department(event.getDepartment())
                .eventId(event.getId())
                .title(event.getTitle())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}