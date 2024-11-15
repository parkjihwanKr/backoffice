package com.example.backoffice.domain.attendance.converter;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.member.entity.Members;

import java.util.ArrayList;
import java.util.List;

public class AttendancesConverter {

    public static Attendances toEntity(Members member){
        return Attendances.builder()
                .attendanceStatus(AttendanceStatus.ABSENT)
                .checkinTime(null)
                .checkoutTime(null)
                .description("스케줄러에 의한 하루 근태 생성")
                .member(member)
                .build();
    }
}
