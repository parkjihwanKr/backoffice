package com.example.backoffice.domain.attendance.converter;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.attendance.exception.AttendancesExceptionCode;
import com.example.backoffice.domain.member.entity.Members;

import java.util.ArrayList;
import java.util.List;

public class AttendancesConverter {

    public static Attendances toEntity(Members member, AttendanceStatus attendanceStatus){
        return Attendances.builder()
                // 초기에 생성되는 status는 결석, 휴가, 휴일
                .attendanceStatus(attendanceStatus)
                .checkInTime(null)
                .checkOutTime(null)
                .description("스케줄러에 의한 하루 근태 생성")
                .member(member)
                .build();
    }

    public static AttendancesResponseDto.UpdateCheckInTimeDto toUpdateCheckInTimeDto(
            Attendances attendances){
        return AttendancesResponseDto.UpdateCheckInTimeDto
                .builder()
                .attendanceId(attendances.getId())
                .checkInTime(attendances.getCheckInTime())
                .memberName(attendances.getMember().getMemberName())
                .attendanceStatus(attendances.getAttendanceStatus())
                .build();
    }

    public static AttendancesResponseDto.UpdateCheckOutTimeDto toUpdateCheckOutTimeDto(
            Attendances attendances){
        return AttendancesResponseDto.UpdateCheckOutTimeDto
                .builder()
                .attendanceId(attendances.getId())
                .checkInTime(attendances.getCheckInTime())
                .checkOutTime(attendances.getCheckOutTime())
                .memberName(attendances.getMember().getMemberName())
                .attendanceStatus(attendances.getAttendanceStatus())
                .build();
    }

    public static AttendancesResponseDto.ReadOneDto toReadOneDto(
            Attendances attendance){
        return AttendancesResponseDto.ReadOneDto.builder()
                .attendanceId(attendance.getId())
                .description(attendance.getDescription())
                .attendanceStatus(attendance.getAttendanceStatus())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .build();
    }

    public static AttendancesResponseDto.UpdateAttendancesStatusDto toUpdateOneStatus(
            Attendances attendance){
        return AttendancesResponseDto.UpdateAttendancesStatusDto.builder()
                .attendanceId(attendance.getId())
                .attendanceStatus(attendance.getAttendanceStatus())
                .description(attendance.getDescription())
                .memberName(attendance.getMember().getMemberName())
                .build();
    }

    public static AttendanceStatus toAttendanceStatus(String attdStatus) {
        for (AttendanceStatus attendanceStatus : AttendanceStatus.values()) {
            if (attendanceStatus.getLabel().equalsIgnoreCase(attdStatus)) {
                return attendanceStatus;
            }
        }
        throw new AttendancesCustomException(AttendancesExceptionCode.NOT_FOUND_ATTENDANCE_STATUS);
    }

    public static AttendancesResponseDto.ReadDto toReadDto(
            List<Attendances> memberAttendanceList){
        return null;
    }

    public static List<AttendancesResponseDto.ReadOneDto> toReadFilteredDto(
            List<Attendances> memberAttendanceList) {
        return memberAttendanceList == null || memberAttendanceList.isEmpty()
                ? new ArrayList<>()
                : memberAttendanceList.stream()
                .map(AttendancesConverter::toReadOneDto)
                .toList();
    }
}