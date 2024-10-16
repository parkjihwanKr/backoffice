package com.example.backoffice.domain.vacation.facade;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;

import java.util.List;

public interface VacationsServiceFacadeV1 {
    VacationsResponseDto.UpdatePeriodDto updatePeriod(
            Members loginMember, VacationsRequestDto.UpdatePeriodDto requestDto);

    VacationsResponseDto.CreateOneDto createOne(
            Members loginMember, VacationsRequestDto.CreateOneDto requestDto);

    VacationsResponseDto.ReadDayDto readDay(
            Long vacationId, Members loginMember);

    VacationsResponseDto.UpdateOneDto updateOne(
            Long vacationId, Members loginMember,
            VacationsRequestDto.UpdateOneDto requestDto);

    VacationsResponseDto.UpdateOneForAdminDto updateOneForAdmin(
            Long vacationId, Members loginMember);

    void deleteOne(Long vacationId, Members loginMember);

    List<VacationsResponseDto.ReadMonthDto> readForHrManager(
            Long year, Long month, Boolean isAccepted, Boolean urgent,
            String department, Members loginMember);

    void deleteOneForHrManager(
            Long vacationId, VacationsRequestDto.DeleteOneForAdminDto requestDto,
            Members loginMember);
}
