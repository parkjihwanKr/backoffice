package com.example.backoffice.domain.attendance.entity;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table
@Entity
@Builder
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
    private LocalDateTime checkinTime;

    // 퇴근 시간 기록
    private LocalDateTime checkoutTime;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    // entity method
}
