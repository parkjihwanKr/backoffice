package com.example.backoffice.domain.attendance.service;

import com.example.backoffice.domain.attendance.converter.AttendancesConverter;
import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import com.example.backoffice.domain.attendance.exception.AttendancesCustomException;
import com.example.backoffice.domain.attendance.exception.AttendancesExceptionCode;
import com.example.backoffice.domain.attendance.repository.AttendancesRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendancesServiceImplV1 implements AttendancesServiceV1{

    private final MembersServiceV1 membersService;
    private final AttendancesRepository attendancesRepository;

    @Override
    @Transactional
    public void create(Boolean isWeekDay){
        List<Members> memberList = membersService.findAll();
        List<Attendances> attendancesList = new ArrayList<>();
        for (Members member : memberList) {
            AttendanceStatus status = member.getOnVacation()
                    ? AttendanceStatus.VACATION
                    : (isWeekDay ? AttendanceStatus.ABSENT : AttendanceStatus.HOLIDAY);
            attendancesList.add(AttendancesConverter.toEntity(member, status));
        }
        attendancesRepository.saveAll(attendancesList);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.UpdateCheckInTimeDto updateCheckInTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            Members loginMember){
        Attendances attendance = findById(attendanceId);
        membersService.matchLoginMember(
                attendance.getMember(), loginMember.getId());
        LocalDateTime checkInTime
                = DateTimeUtils.parse(requestDto.getCheckInTime());

        AttendanceStatus attendanceStatus
                = validateAndDetermineCheckInStatus(checkInTime);
        attendance.updateCheckIn(
                checkInTime, attendanceStatus);

        return AttendancesConverter.toUpdateCheckInTimeDto(attendance);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.UpdateCheckOutTimeDto updateCheckOutTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            Members loginMember){
        Attendances attendance = findById(attendanceId);
        membersService.matchLoginMember(
                attendance.getMember(), loginMember.getId());
        LocalDateTime checkOutTime
                = DateTimeUtils.parse(requestDto.getCheckOutTime());

        // 정시 출근, 지각, 결석(결석인데 체크하는 건 이상함)
        AttendanceStatus beforeCheckOutStatus = attendance.getAttendanceStatus();
        AttendanceStatus afterCheckOutStatus
                = validateAndDetermineCheckOutStatus(
                        attendance.getCheckOutTime(),
                checkOutTime, beforeCheckOutStatus);

        attendance.updateCheckOut(
                attendance.getCheckOutTime(), requestDto.getDescription(),
                afterCheckOutStatus);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendancesResponseDto.ReadOneDto> readFiltered(
            Long memberId, Long year, Long month,
            String attendanceStatus, Members loginMember){
        // 1. 로그인 멤버와 조회하려는 아이디가 일치하는지?
        membersService.matchLoginMember(loginMember, memberId);

        // 2. 날짜 범위 설정
        LocalDateTime startDate = (year != null && month != null)
                ? DateTimeUtils.getStartDayOfMonth(year, month)
                : null;

        LocalDateTime endDate = (year != null && month != null)
                ? DateTimeUtils.getEndDayOfMonth(year, month)
                : null;

        // 3. AttendanceStatus 변환
        AttendanceStatus attdStatus = (attendanceStatus != null)
                ? AttendancesConverter.toAttendanceStatus(attendanceStatus)
                : null;

        // 4. 동적 필터링된 데이터 조회
        List<Attendances> memberAttendanceList
                = attendancesRepository.findFiltered(
                        memberId, startDate, endDate, attdStatus);

        // 5. DTO 반환
        return AttendancesConverter.toReadFilteredDto(memberAttendanceList);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendancesResponseDto.ReadOneDto readOne(
            Long attendanceId, Members loginMember){
        // 1. 출결 기록의 소유자인지 확인
        Attendances attendance = findById(attendanceId);

        // 1-1. 소유자의 아이디와 로그인 사용자의 아이디와 같을 때
        if(!attendance.getMember().getId().equals(loginMember.getId())){
            // 1-2. 로그인 사용자와 hrManager 또는 사장의 아이디가 같을 때
            Members hrManagerOrCeo = membersService.findHRManager();
            if(!hrManagerOrCeo.getId().equals(loginMember.getId())){
                throw new AttendancesCustomException(AttendancesExceptionCode.RESTRICTED_ACCESS);
            }
        }

        return AttendancesConverter.toReadOneDto(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttendancesResponseDto.ReadOneDto> readForAdmin(
            String memberName, String attendanceStatus,
            DateRange checkInRange, DateRange checkOutRange,
            Members loginMember, Pageable pageable){
        // 1. 접근할 수 있는 권한을 가지는지?
        membersService.findHRManagerOrCEO(loginMember);

        // 2. memberName 확인
        Members foundMember = null;
        if(memberName != null){
            foundMember = membersService.findByMemberName(memberName);
        }

        // 3. 유효한 체크인, 체크아웃 시간 설정인지 확인
        // 해당 날짜가 종료일보다 시작일이 빠른지
        DateTimeUtils.validateStartAndEndDate(
                checkInRange.getStartDate(), checkInRange.getEndDate());
        DateTimeUtils.validateStartAndEndDate(
                checkOutRange.getStartDate(), checkOutRange.getEndDate());

        // 4. AttendanceStatus 변환
        AttendanceStatus attdStatus = (attendanceStatus != null)
                ? AttendancesConverter.toAttendanceStatus(attendanceStatus)
                : null;

        // 5. 필터링된 데이터 조회
        Page<Attendances> memberAttendancePage = attendancesRepository.findFilteredForAdmin(
                foundMember.getId(),
                attdStatus,
                checkInRange != null ? checkInRange.getStartDate() : null,
                checkInRange != null ? checkInRange.getEndDate() : null,
                checkOutRange != null ? checkOutRange.getStartDate() : null,
                checkOutRange != null ? checkOutRange.getEndDate() : null,
                pageable);

        return memberAttendancePage.map(AttendancesConverter::toReadOneDto);
    }

    @Override
    @Transactional
    public AttendancesResponseDto.UpdateAttendancesStatusDto updateOneStatusForAdmin(
            Long memberId, Long attendanceId, Members loginMember,
            AttendancesRequestDto.UpdateAttendanceStatusDto requestDto){
        // 1. 변경하려는 대상과 근태 기록이 존재하는지
        membersService.findById(memberId);
        Attendances attendance
                = attendancesRepository.findByMemberId(memberId).orElseThrow(
                ()-> new AttendancesCustomException(AttendancesExceptionCode.NOT_FOUND_ATTENDANCES));

        // 2. 근태 기록 변경 권한이 있는지
        Members hrManagerOrCeo = membersService.findHRManagerOrCEO(loginMember);
        if(hrManagerOrCeo == null){
            throw new AttendancesCustomException(AttendancesExceptionCode.RESTRICTED_ACCESS);
        }

        // 3. 변경하려는 근태 상태가 적절하게 요청되었는지
        AttendanceStatus requestedAttendanceStatus
                = AttendancesConverter.toAttendanceStatus(
                        requestDto.getAttendanceStatus());

        // 4. 요청하려는 근태 상태와 변경하려는 근태 상태가 같진 않은지
        if(attendance.getAttendanceStatus().equals(requestedAttendanceStatus)){
            throw new AttendancesCustomException(AttendancesExceptionCode.EQUALS_TO_ATTENDANCES_STATUS);
        }

        // 5. 해당 근태 상태 변경
        attendance.updateStatusAndDescription(
                requestedAttendanceStatus, requestDto.getDescription());

        // 6. DTO 반환
        return AttendancesConverter.toUpdateOneStatus(attendance);
    }

    @Transactional(readOnly = true)
    public Attendances findById(Long attendancesId){
        return attendancesRepository.findById(attendancesId).orElseThrow(
                ()-> new AttendancesCustomException(AttendancesExceptionCode.NOT_FOUND_ATTENDANCES)
        );
    }

    private AttendanceStatus validateAndDetermineCheckInStatus(LocalDateTime checkInTime){
        // 1. 출근 시간 유효한지?
        validateTime(checkInTime);

        // 2. 유효한 출근 시간인지 검증
        if(checkInTime.getHour() < 8 || checkInTime.getHour() > 14){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.CHECK_IN_TIME_OUTSIDE_WORK_HOURS);
        }

        // 3. 오전 9시를 넘은 시간에 출근했을 때
        if(checkInTime.getHour() > DateTimeUtils.getTodayCheckInTime().getHour()
                || (checkInTime.getHour() == DateTimeUtils.getTodayCheckInTime().getHour()
                && checkInTime.getMinute() > 1)){
            return AttendanceStatus.LATE;
        }

        return AttendanceStatus.ON_TIME;
    }

    private AttendanceStatus validateAndDetermineCheckOutStatus(
            LocalDateTime checkInTime, LocalDateTime checkOutTime, AttendanceStatus beforeCheckOutStatus){
        validateTime(checkOutTime);

        // 1. 결석 처리: 출근-퇴근 시간이 2시간 이하
        if (DateTimeUtils.isWithinHours(checkInTime, checkOutTime, 2)) {
            return AttendanceStatus.ABSENT;
        }

        // 2. 조퇴 처리: 퇴근 시간이 정상 퇴근 시간보다 이른 경우
        if (DateTimeUtils.isBeforeTodayCheckOutTime(checkOutTime)) {
            return AttendanceStatus.HALF_DAY;
        }

        // 3. 지각 인원은 그대로 지각 처리
        if(beforeCheckOutStatus.equals(AttendanceStatus.LATE)){
            return beforeCheckOutStatus;
        }

        // 4. 정상 퇴근 처리
        return AttendanceStatus.ON_TIME;
    }

    private void validateTime(LocalDateTime time){
        LocalDate tomorrow = DateTimeUtils.getTomorrow().toLocalDate();
        LocalDate today = DateTimeUtils.getToday().toLocalDate();

        // 1. 파라미터로 받는 시간이 오늘 이전인지
        if(!time.toLocalDate().isAfter(today)){
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.TIME_BEFORE_TODAY);
        }

        // 2. 파라미터로 받는 시간이 내일 이후로 시간이 되어있진 않은지
        if (!time.toLocalDate().isBefore(tomorrow)) {
            throw new AttendancesCustomException(
                    AttendancesExceptionCode.TIME_EQUAL_OR_AFTER_TOMORROW);
        }
    }
}
