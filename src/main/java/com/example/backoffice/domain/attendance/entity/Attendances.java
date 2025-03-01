package com.example.backoffice.domain.attendance.entity;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        indexes = {
                @Index(name = "idx_attendances_member_id", columnList = "member_id"),
                @Index(name = "idx_attendances_member_created", columnList = "member_id, created_at") // 복합 인덱스 추가
        }
)
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
        this.checkOutTime = checkoutTime;
        this.description = description;
        this.attendanceStatus = attendanceStatus;
    }

    public void update(
            AttendanceStatus requestedAttendanceStatus, String description,
            LocalDateTime checkInTime, LocalDateTime checkOutTime){
        this.attendanceStatus = requestedAttendanceStatus;
        this.description = description;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public void updateDescriptionAndStatus(
            String description, AttendanceStatus status){
        this.attendanceStatus = status;
        this.description = description;
    }
}
