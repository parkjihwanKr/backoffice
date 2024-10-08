package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.vacation.entity.Vacations;

import java.time.LocalDateTime;
import java.util.List;

public interface VacationsServiceV1 {

    Vacations save(Vacations vacation);

    List<Vacations> findVacationsOnDate(LocalDateTime startDate);

    List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findByMemberIdVacationOnDate(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAllByMemberIdAndStartDate(Long memberId, LocalDateTime startDate);

    void deleteById(Long vacationId);

    Boolean existsByOnVacationMemberId(Long loginMemberId);

    Long countVacationingMembers(LocalDateTime customStartDate);

    Vacations findById(Long vacationId);

    List<Vacations> findAllByEndDateBefore(LocalDateTime now);

    List<Vacations> findAllByStartDate(LocalDateTime now);

}