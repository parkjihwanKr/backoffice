package com.example.backoffice.domain.vacation.repository;


import com.example.backoffice.domain.vacation.entity.Vacations;

import java.time.LocalDateTime;
import java.util.List;

public interface VacationsQuery {
    long countVacationingMembers(LocalDateTime customStartDate);

    List<Vacations> findVacationsOnDate(LocalDateTime startDate);

    List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAllByEndDateBefore(LocalDateTime now);

    List<Vacations> findAllByStartDate(LocalDateTime now);

    List<Vacations> findByMemberIdVacationOnDate(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAllByMemberIdAndStartDate(
            Long memberId, LocalDateTime startDate);
}
