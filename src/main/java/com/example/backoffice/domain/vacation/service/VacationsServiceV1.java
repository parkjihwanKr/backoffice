package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;

import java.util.List;

public interface VacationsServiceV1 {

    VacationsResponseDto.CreateOneDto createOne(
            Members loginMember, VacationsRequestDto.CreateOneDto requestDto);

    List<VacationsResponseDto.ReadDayDto> readDay(
            String department, Long year, Long month, Long day, Members loginMember);

    List<VacationsResponseDto.ReadMonthForDepartmentDto> readMonthForDepartment(
            String department, Long year, Long month, Members loginMember);

    VacationsResponseDto.UpdateOneDto updateOne(
            Long vacationId, Members loginMember,
            VacationsRequestDto.UpdateOneDto requestDto);

    void deleteOne(Long vacationId, Members loginMember);
}
