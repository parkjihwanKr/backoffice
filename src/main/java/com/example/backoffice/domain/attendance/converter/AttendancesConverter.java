package com.example.backoffice.domain.attendance.converter;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.member.entity.Members;

public class AttendancesConverter {

    public static Attendances toEntity(Members member, AttendanceStatus attendanceStatus){
        return Attendances.builder()
                // 초기에 생성되는 status는 결석, 휴가, 휴일
                .attendanceStatus(attendanceStatus)
                .checkinTime(null)
                .checkoutTime(null)
                .description("스케줄러에 의한 하루 근태 생성")
                .member(member)
                .build();
    }

    public static AttendancesResponseDto.UpdateCheckInTimeDto toUpdateCheckInTimeDto(
            Attendances attendances){
        return AttendancesResponseDto.UpdateCheckInTimeDto
                .builder()
                .checkInTime(attendances.getCheckinTime())
                .memberName(attendances.getMember().getMemberName())
                .attendanceStatus(attendances.getAttendanceStatus())
                .build();
    }
}
