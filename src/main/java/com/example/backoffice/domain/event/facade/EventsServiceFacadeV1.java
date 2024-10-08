package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.entity.Events;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventsServiceFacadeV1 {

    EventsResponseDto.CreateOneForCompanyEventDto createOneForCompany(
            EventsRequestDto.CreateOneForCompanyEventDto requestDto ,Members loginMember);

    EventsResponseDto.ReadOneForCompanyEventDto readOneForCompanyEvent(Long eventId);

    List<EventsResponseDto.ReadOneForCompanyEventDto> readForCompanyMonthEvent(
            Long year, Long month);

    List<List<EventsResponseDto.ReadOneForCompanyEventDto>> readForCompanyYearEvent(
            Long year);

    EventsResponseDto.UpdateOneForCompanyEventDto updateOneForCompany(
            Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForCompanyEventDto requestDto,
            List<MultipartFile> files);

    void deleteOneForCompany(Long eventId, Members loginMember)   ;

    EventsResponseDto.CreateOneForDepartmentEventDto createOneForDepartmentEvent(
            String department, Members loginMember,
            EventsRequestDto.CreateOneForDepartmentEventDto requestDto,
            List<MultipartFile> files);

    List<EventsResponseDto.ReadOneForDepartmentEventDto> readForDepartmentMonthEvent(
        String department, Long year, Long month, Members loginMember);

    EventsResponseDto.UpdateOneForDepartmentEventDto updateOneForDepartmentEvent(
            String department, Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneForDepartmentEventDto requestDto,
            List<MultipartFile> files);

    void deleteOneForDepartmentEvent(String department, Long eventId, Members loginMember);

    List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberSchedule(
            Long memberId, Long year, Long month, Members loginMember);

    List<EventsResponseDto.ReadOneForMemberScheduleDto> readForMemberDaySchedule(
            Long memberId, Long year, Long month, Long day, Members loginMember);

}
