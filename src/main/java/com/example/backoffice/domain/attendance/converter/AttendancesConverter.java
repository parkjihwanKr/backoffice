package com.example.backoffice.domain.attendance.converter;

import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.attendance.exception.AttendancesExceptionCode;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
            LocalDateTime checkInTime, LocalDateTime checkOutTime){
        return Attendances.builder()
                // 초기에 생성되는 status는 결석, 휴가, 휴일
                .attendanceStatus(attendanceStatus)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
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
                .memberName(attendance.getMember().getMemberName())
                .description(attendance.getDescription())
                .attendanceStatus(attendance.getAttendanceStatus())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .createdAt(attendance.getCreatedAt())
                .build();
    }

    public static AttendancesResponseDto.UpdateAttendancesStatusDto toUpdateOneStatus(
            Attendances attendance){
        return AttendancesResponseDto.UpdateAttendancesStatusDto.builder()
                .attendanceId(attendance.getId())
                .memberId(attendance.getMember().getId())
                .attendanceStatus(attendance.getAttendanceStatus())
                .description(attendance.getDescription())
                .memberName(attendance.getMember().getMemberName())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
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
            List<Attendances> attendanceList, int pageNumber) {

        // 날짜별로 그룹화하여 각 날짜의 근태 요약 정보를 생성
        Map<LocalDate, List<Attendances>> groupedByDate = attendanceList.stream()
                .collect(Collectors.groupingBy(
                        attendance -> attendance.getCreatedAt().toLocalDate()
                ));

        // 날짜를 정렬하여 구간 생성
        List<LocalDate> sortedDates = groupedByDate.keySet().stream()
                .sorted()
                .toList();

        // 날짜 범위를 구간별로 나눔
        List<List<LocalDate>> dateRanges = Arrays.asList(
                sortedDates.stream().filter(
                        date -> date.getDayOfMonth() <= 7).toList(),
                sortedDates.stream().filter(
                        date -> date.getDayOfMonth() >= 8 && date.getDayOfMonth() <= 14).toList(),
                sortedDates.stream().filter(
                        date -> date.getDayOfMonth() >= 15 && date.getDayOfMonth() <= 21).toList(),
                sortedDates.stream().filter(
                        date -> date.getDayOfMonth() >= 22 && date.getDayOfMonth() <= 28).toList(),
                sortedDates.stream().filter(
                        date -> date.getDayOfMonth() >= 29).toList()
        );

        // 총 페이지 수를 날짜 구간에 따라 동적으로 계산
        int fixedTotalPages
                = (int) dateRanges.stream().filter(
                        range -> !range.isEmpty()).count();

        // 페이지 번호 유효성 검증
        if (pageNumber < 0 || pageNumber >= fixedTotalPages) {
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.NOT_EXIST_ATTENDANCE_PAGE);
        }

        // 페이지에 해당하는 날짜 그룹 선택
        List<LocalDate> selectedDates = dateRanges.get(pageNumber);

        // 선택된 날짜에 해당하는 데이터를 필터링
        List<AttendancesResponseDto.ReadMonthlyDto> monthlyDtoList = groupedByDate.entrySet().stream()
                .filter(entry -> selectedDates.contains(entry.getKey())) // 선택된 날짜만 포함
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Attendances> dailyAttendances = entry.getValue();

                    // 각 상태별 카운트 계산
                    return AttendancesResponseDto.ReadMonthlyDto.builder()
                            .createdAt(date.atStartOfDay())
                            .absentCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.ABSENT)
                                    .count())
                            .onTimeCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.ON_TIME)
                                    .count())
                            .onVacationCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.VACATION)
                                    .count())
                            .lateCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.LATE)
                                    .count())
                            .halfDayCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.HALF_DAY)
                                    .count())
                            .outOfOfficeCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.OUT_OF_OFFICE)
                                    .count())
                            .holidayCount((int) dailyAttendances.stream()
                                    .filter(att -> att.getAttendanceStatus() == AttendanceStatus.HOLIDAY)
                                    .count())
                            .build();
                }).toList();

        // 페이지 크기 계산: 한 날짜의 멤버 수 * 선택된 날짜 수
        int membersPerDay = groupedByDate.isEmpty() ? 0 : groupedByDate.values().iterator().next().size();
        int pageSize = membersPerDay * selectedDates.size();

        // 전체 요소 수
        long totalElements = attendanceList.size();

        // PageImpl 객체 생성
        Page<AttendancesResponseDto.ReadMonthlyDto> customPage = new PageImpl<>(
                monthlyDtoList,
                PageRequest.of(pageNumber, pageSize),
                totalElements
        );

        return customPage;
    }


    public static Page<AttendancesResponseDto.ReadOneDto> toReadFilteredDailyDto(
            Page<Attendances> attendancePage) {

        // Attendances 엔티티를 ReadOneDto로 변환
        List<AttendancesResponseDto.ReadOneDto> memberAttendanceList = attendancePage.getContent().stream()
                .map(attendance -> AttendancesResponseDto.ReadOneDto.builder()
                        .attendanceId(attendance.getId())
                        .memberId(attendance.getMember().getId())
                        .memberName(attendance.getMember().getMemberName())
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

    public static AttendancesResponseDto.ReadScheduledRecordDto toReadScheduleRecordDto(
            int index, Long memberId, String memberName, MemberDepartment department,
            String description, LocalDateTime startDate, LocalDateTime endDate){
        return AttendancesResponseDto.ReadScheduledRecordDto.builder()
                .index(index)
                .memberId(memberId)
                .memberName(memberName)
                .department(department)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public static List<AttendancesResponseDto.ReadSummaryOneDto> toReadSummaryListDto(
            List<Attendances> personalAttendanceList){
        List<AttendancesResponseDto.ReadSummaryOneDto> personalAttendanceDtoList = new ArrayList<>();
        for(int i = 0; i<personalAttendanceList.size(); i++){
            personalAttendanceDtoList.add(
                    AttendancesConverter.toReadSummaryOneDto(
                            personalAttendanceList.get(i)));
        }
        return personalAttendanceDtoList;
    }

    public static AttendancesResponseDto.ReadSummaryOneDto toReadSummaryOneDto(
            Attendances attendance){
        return AttendancesResponseDto.ReadSummaryOneDto.builder()
                .attendanceId(attendance.getId())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .memberName(attendance.getMember().getMemberName())
                .attendanceStatus(attendance.getAttendanceStatus())
                .createdAt(attendance.getCreatedAt())
                .build();
    }

    public static AttendancesResponseDto.ReadTodayOneDto toReadTodayOneDto(
            Attendances attendance){
        return AttendancesResponseDto.ReadTodayOneDto.builder()
                .attendanceId(attendance.getId())
                .attendanceStatus(attendance.getAttendanceStatus())
                .build();
    }
}