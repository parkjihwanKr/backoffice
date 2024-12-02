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

    public static Attendances toEntity(
            Members member, AttendanceStatus attendanceStatus,
            String description, LocalDateTime checkInTime, LocalDateTime checkOutTime){
        return Attendances.builder()
                // 초기에 생성되는 status는 결석, 휴가, 휴일
                .attendanceStatus(attendanceStatus)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .description(description)
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
                .memberId(attendance.getMember().getId())
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

        // 날짜별로 그룹화하여 각 날짜의 근태 요약 정보를 생성
        Map<LocalDate, List<Attendances>> groupedByDate =
                attendancePage.getContent().stream()
                        .collect(Collectors.groupingBy(
                                attendance -> attendance.getCreatedAt().toLocalDate() // 날짜 기준으로 그룹화
                        ));

        // 그룹화된 데이터를 이용해 ReadMonthlyDto 리스트 생성
        List<AttendancesResponseDto.ReadMonthlyDto> monthlyDtoList = groupedByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Attendances> dailyAttendances = entry.getValue();

                    // 각 상태별 카운트 계산
                    int absentCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.ABSENT)
                            .count();
                    int onTimeCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.ON_TIME)
                            .count();
                    int onVacationCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.VACATION)
                            .count();
                    int outOfOfficeCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.OUT_OF_OFFICE)
                            .count();
                    int lateCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.LATE)
                            .count();
                    int halfDayCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.HALF_DAY)
                            .count();
                    int holidayCount = (int) dailyAttendances.stream()
                            .filter(att -> att.getAttendanceStatus() == AttendanceStatus.HOLIDAY)
                            .count();

                    // ReadMonthlyDto 생성
                    return AttendancesResponseDto.ReadMonthlyDto.builder()
                            .createdAt(date.atStartOfDay()) // 해당 날짜의 시작 시간
                            .absentCount(absentCount)
                            .onTimeCount(onTimeCount)
                            .onVacationCount(onVacationCount)
                            .lateCount(lateCount)
                            .halfDayCount(halfDayCount)
                            .outOfOfficeCount(outOfOfficeCount)
                            .holidayCount(holidayCount)
                            .build();
                }).toList();

        // PageImpl 객체로 변환하여 반환
        return new PageImpl<>(monthlyDtoList, attendancePage.getPageable(), attendancePage.getTotalElements());
    }

    public static Page<AttendancesResponseDto.ReadOneDto> toReadFilteredDailyDto(
            Page<Attendances> attendancePage) {

        // Attendances 엔티티를 ReadOneDto로 변환
        List<AttendancesResponseDto.ReadOneDto> memberAttendanceList = attendancePage.getContent().stream()
                .map(attendance -> AttendancesResponseDto.ReadOneDto.builder()
                        .attendanceId(attendance.getId())
                        .memberId(attendance.getMember().getId())
                        .memberName(attendance.getMember().getName())
                        .checkInTime(attendance.getCheckInTime())
                        .checkOutTime(attendance.getCheckOutTime())
                        .attendanceStatus(attendance.getAttendanceStatus())
                        .description(attendance.getDescription())
                        .build())
                .toList();

        // 변환된 데이터를 PageImpl로 반환
        return new PageImpl<>(
                memberAttendanceList,
                attendancePage.getPageable(),
                attendancePage.getTotalElements()
        );
    }
}