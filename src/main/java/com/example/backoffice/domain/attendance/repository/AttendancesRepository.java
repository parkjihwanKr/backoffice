package com.example.backoffice.domain.attendance.repository;

import com.example.backoffice.domain.attendance.entity.Attendances;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendancesRepository extends JpaRepository<Attendances, Long>, AttendancesQuery {
    Optional<Attendances> findByIdAndMemberId(Long attendanceId, Long memberId);
    boolean existsByMemberIdAndCreatedAt(Long memberId, LocalDate createdAt);
}
