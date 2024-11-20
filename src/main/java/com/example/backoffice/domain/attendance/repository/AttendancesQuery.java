package com.example.backoffice.domain.attendance.repository;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendancesQuery {

    List<Attendances> findFiltered(
            Long memberId, LocalDateTime startDate,
            LocalDateTime endDate, AttendanceStatus attdStatus);

    Page<Attendances> findFilteredForAdmin(
            Long foundMemberId, AttendanceStatus attendanceStatus,
            LocalDateTime checkInStartTime, LocalDateTime checkInEndTime,
            LocalDateTime checkOutStartTime, LocalDateTime checkOutEndTime,
            Pageable pageable);

    void deleteBeforeTwoYear(
            List<Long> allMemberIdList,
            LocalDateTime startOfDeletion,
            LocalDateTime endOfDeletion);

    Attendances findByMemberIdAndCreatedDate(
            Long memberId, LocalDate createdDate);

    void saveManually(
            Long memberId, LocalDateTime customCreatedAt,
            Attendances attendance);
}
