package com.example.backoffice.domain.attendance.converter;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.attendance.exception.AttendancesExceptionCode;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static Attendances toEntityForAdmin(
            Members member, LocalDateTime checkInTime,
            LocalDateTime checkOutTime, AttendanceStatus attendanceStatus,
            String description){
        return Attendances.builder()
                // 초기에 생성되는 status는 결석, 휴가, 휴일
                .attendanceStatus(attendanceStatus)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .description(description)
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

    public static List<AttendancesResponseDto.ReadOneDto> toReadFilteredDto(
            List<Attendances> memberAttendanceList) {
        return memberAttendanceList == null || memberAttendanceList.isEmpty()
                ? new ArrayList<>()
                : memberAttendanceList.stream()
                .map(AttendancesConverter::toReadOneDto)
                .toList();
    }

    public static AttendancesResponseDto.CreateOneDto toCreateOneForAdminDto(
            String memberName, AttendanceStatus attendanceStatus, String description){
        return AttendancesResponseDto.CreateOneDto.builder()
                .memberName(memberName)
                .attendanceStatus(attendanceStatus)
                .description(description)
                .build();
    }

    public static Page<AttendancesResponseDto.ReadMonthlyDto> toReadFilteredMonthlyDto(
            Page<Attendances> attendancePage) {

        Map<LocalDate, Map<Long, List<Attendances>>> groupedAttendances =
                attendancePage.getContent().stream()
                        .collect(Collectors.groupingBy(
                                attendance -> attendance.getCreatedAt().toLocalDate(), // Group by date
                                Collectors.groupingBy(attendance -> attendance.getMember().getId()) // Group by member ID
                        ));

        List<AttendancesResponseDto.ReadMonthlyDto> monthlyDtoList = groupedAttendances.entrySet().stream()
                .map(dateEntry -> {
                    List<AttendancesResponseDto.ReadDayDto> dayDtoList = dateEntry.getValue().entrySet().stream()
                            .map(memberEntry -> {
                                List<Attendances> memberAttendances = memberEntry.getValue();
                                Members member = memberAttendances.get(0).getMember();

                                int absentCount = (int) memberAttendances.stream()
                                        .filter(att -> att.getAttendanceStatus() == AttendanceStatus.ABSENT)
                                        .count();
                                int onTimeCount = (int) memberAttendances.stream()
                                        .filter(att -> att.getAttendanceStatus() == AttendanceStatus.ON_TIME)
                                        .count();
                                int onVacationCount = (int) memberAttendances.stream()
                                        .filter(att -> att.getAttendanceStatus() == AttendanceStatus.VACATION)
                                        .count();
                                int outOfOfficeCount = (int) memberAttendances.stream()
                                        .filter(att -> att.getAttendanceStatus() == AttendanceStatus.OUT_OF_OFFICE)
                                        .count();

                                return AttendancesResponseDto.ReadDayDto.builder()
                                        .attendanceId(memberEntry.getKey()) // Unique member ID
                                        .memberName(member.getName()) // Member name
                                        .absentCount(absentCount)
                                        .onTimeCount(onTimeCount)
                                        .onVacationCount(onVacationCount)
                                        .outOfOfficeCount(outOfOfficeCount)
                                        .build();
                            }).toList();

                    return AttendancesResponseDto.ReadMonthlyDto.builder()
                            .dayDtoList(dayDtoList)
                            .build();
                }).toList();

        return new PageImpl<>(monthlyDtoList, attendancePage.getPageable(), attendancePage.getTotalElements());
    }
}