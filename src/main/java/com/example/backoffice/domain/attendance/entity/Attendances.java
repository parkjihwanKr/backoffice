package com.example.backoffice.domain.attendance.entity;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table
@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Attendances extends CommonEntity {

    // fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    private String description;

    // 출근 시간 기록
    private LocalDateTime checkInTime;

    // 퇴근 시간 기록
    private LocalDateTime checkOutTime;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    // entity method
    public void updateCheckIn(
            LocalDateTime checkinTime,
            AttendanceStatus attendanceStatus){
        this.checkInTime = checkinTime;
        this.description = null;
        this.attendanceStatus = attendanceStatus;
    }

    public void updateCheckOut(
            LocalDateTime checkoutTime, String description,
            AttendanceStatus attendanceStatus){
        this.checkInTime = checkoutTime;
        this.description = description;
        this.attendanceStatus = attendanceStatus;
    }

    public void updateOneForToday(
            AttendanceStatus requestedAttendanceStatus,
            String description,
            LocalDateTime checkInTime){
        this.attendanceStatus = requestedAttendanceStatus;
        this.description = description;
        this.checkInTime = checkInTime;
        this.checkOutTime = null;
    }

    public void updateOneForBeforeToday (
            AttendanceStatus requestedAttendanceStatus,
            String description,
            LocalDateTime checkInTime, LocalDateTime checkOutTime){
        this.attendanceStatus = requestedAttendanceStatus;
        this.description = description;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public void updateOneForOutside(
            AttendanceStatus requestedAttendanceStatus,
            String description){
        this.attendanceStatus = requestedAttendanceStatus;
        this.description = description;
    }

    public void updateOneForHalfDay(
            AttendanceStatus requestedAttendanceStatus,
            String description,
            LocalDateTime checkInTime, LocalDateTime checkOutTime){
        this.attendanceStatus = requestedAttendanceStatus;
        this.description = description;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }
}
