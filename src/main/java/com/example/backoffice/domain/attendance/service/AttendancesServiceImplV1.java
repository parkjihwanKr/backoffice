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
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
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

    /**
     * @param isWeekDay : 평일 : 휴일 = true : false
     * 스케줄러를 통한 모든 멤버의 출석 기록을 자동 생성
     */
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

    /**
     *
     * @param attendanceId : 근태 고유 아이디
     * @param requestDto : 요청 출근 시간
     * @param loginMember : 로그인 사용자
     * @return 출근한 사용자 이름, 출근 시간, 근태 상태
     */
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

    /**
     * 상태 : ON_DATE, ABSENT,
     * @param attendanceId : 근태 고유 아이디
     * @param requestDto : 퇴근 시간, 상태, 설명,
     * @param loginMember : 로그인 사용자
     * @return 퇴근자 이름, 출근 시간, 퇴근 시간, 근태 상태
     */
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
                        attendance.getCheckoutTime(),
                checkOutTime, beforeCheckOutStatus);

        attendance.updateCheckOut(
                attendance.getCheckoutTime(), requestDto.getDescription(),
                afterCheckOutStatus);
        return null;
    }

    /**
     *
     * @param attendancesId : 근태 고유 아이디
     * @return 근태 정보
     */
    @Transactional(readOnly = true)
    public Attendances findById(Long attendancesId){
        return attendancesRepository.findById(attendancesId).orElseThrow(
                ()-> new AttendancesCustomException(AttendancesExceptionCode.NOT_FOUND_ATTENDANCES)
        );
    }

    /**
     *
     * @param checkInTime : 출근 시간
     * @return 근태 상태
     */
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

    /**
     *
     * @param checkInTime : 출근 시간
     * @param checkOutTime : 퇴근 시간
     * @param beforeCheckOutStatus : 퇴근 전 근태 상태
     * @return 퇴근 후 근태 상태
     */
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

    /**
     * @param time : 출근 시간, 퇴근 시간
     */
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
