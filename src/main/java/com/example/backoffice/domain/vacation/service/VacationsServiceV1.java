package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;

import java.util.List;

public interface VacationsServiceV1 {

    VacationsResponseDto.UpdatePeriodDto updatePeriod(
            Members loginMember, VacationsRequestDto.UpdatePeriodDto requestDto);

    VacationsResponseDto.CreateOneDto createOne(
            Members loginMember, VacationsRequestDto.CreateOneDto requestDto);

    VacationsResponseDto.ReadDayDto readDay(
            Long vacationId, Members loginMember);

    List<VacationsResponseDto.ReadDayDto> readDayForAdmin(
            String department, Long year, Long month, Long day, Members loginMember);

    List<VacationsResponseDto.ReadMonthDto> readMonthForAdmin(
            String department, Long year, Long month, Members loginMember);

    VacationsResponseDto.UpdateOneDto updateOne(
            Long vacationId, Members loginMember,
            VacationsRequestDto.UpdateOneDto requestDto);

    void deleteOne(Long vacationId, Members loginMember);

}
