package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.entity.Vacations;

import java.time.LocalDateTime;
import java.util.List;

public interface VacationsServiceV1 {

    Vacations save(Vacations vacation);

    List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAcceptedVacationByMemberIdAndDateRange(
            Long memberId, Boolean isAccepted,
            LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAllByMemberIdAndStartDate(Long memberId, LocalDateTime startDate);

    void deleteById(Long vacationId);

    Long countVacationingMembers(LocalDateTime customStartDate);

    Vacations findById(Long vacationId);

    List<Vacations> findAllByEndDateBefore(LocalDateTime now);

    List<Vacations> findAllByStartDate(LocalDateTime now);

    Boolean existsVacationForMemberInDateRange(
            Long vacationId, Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findFilteredVacationsOnMonth(
            LocalDateTime startDate, LocalDateTime endDate,
            Boolean isAccepted, Boolean urgent, MemberDepartment memberDepartment);

    List<VacationsResponseDto.ReadSummaryOneDto> getPersonalVacationDtoList(
            Long memberId);
}