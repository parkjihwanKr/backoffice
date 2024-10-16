package com.example.backoffice.domain.vacation.repository;


import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.entity.Vacations;

import java.time.LocalDateTime;
import java.util.List;

public interface VacationsQuery {
    long countVacationingMembers(LocalDateTime customStartDate);

    List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAllByEndDateBefore(LocalDateTime now);

    List<Vacations> findAllByStartDate(LocalDateTime now);

    List<Vacations> findAcceptedVacationByMemberIdAndDateRange(
            Long memberId, Boolean isAccepted,
            LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findAllByMemberIdAndStartDate(
            Long memberId, LocalDateTime startDate);

    Boolean existsVacationForMemberInDateRange(
            Long vacationId, Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<Vacations> findFilteredVacationsOnMonth(
            LocalDateTime startDate, LocalDateTime endDate,
            Boolean isAccepted, Boolean urgent, MemberDepartment memberDepartment);
}
