package com.example.backoffice.domain.attendance.service;

import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.common.DateRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendancesServiceV1 {

    /**
     * @param isWeekDay : 평일 : 휴일 = true : false
     * 스케줄러를 통한 모든 멤버의 출석 기록을 자동 생성
     */
    void create(Boolean isWeekDay);

    /**
     *
     * @param attendanceId : 근태 고유 아이디
     * @param requestDto : 요청 출근 시간
     * @param loginMember : 로그인 사용자
     * @return 출근한 사용자 이름, 출근 시간, 근태 상태
     */
    AttendancesResponseDto.UpdateCheckInTimeDto updateCheckInTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            Members loginMember);

    /**
     * 상태 : ON_DATE, ABSENT,
     * @param attendanceId : 근태 고유 아이디
     * @param requestDto : 퇴근 시간, 상태, 설명,
     * @param loginMember : 로그인 사용자
     * @return 퇴근자 이름, 출근 시간, 퇴근 시간, 근태 상태
     */
    AttendancesResponseDto.UpdateCheckOutTimeDto updateCheckOutTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            Members loginMember);

    /**
     *
     * @param memberId : 멤버 아이디
     * @param year : 조회하고 싶은 근태 관리 년도
     * @param month : 조회하고 싶은 근태 관리 월
     * @param attendanceStatus : 조회하고 싶은 근태 상태
     * @param loginMember : 로그인 멤버
     * @return 해당 멤버의 년, 월에 대한 근태 정보 리스트
     */
    List<AttendancesResponseDto.ReadOneDto> readFiltered(
            Long memberId, Long year, Long month,
            String attendanceStatus, Members loginMember);

    /**
     *
     * @param attendanceId : 근태 아이디
     * @param loginMember : 로그인 멤버
     * @return 근태 한 개의 정보
     */
    AttendancesResponseDto.ReadOneDto readOne(
            Long attendanceId, Members loginMember);

    /**
     *
     * @param memberName : 필터링하고 싶은 멤버 이름
     * @param attendanceStatus : 필터링하고 싶은 근태 상태
     * @param checkInRange : 필터링하고 싶은 출근 시간 범위
     * @param checkOutRange : 필터링하고 싶은 퇴근 시간 범위
     * @param loginMember : 로그인 멥버
     * @param pageable : 페이징 정렬
     * @return 관리자를 위한 근태 관리 페이지 정보
     */
    Page<AttendancesResponseDto.ReadOneDto> readForAdmin(
            String memberName, String attendanceStatus,
            DateRange checkInRange, DateRange checkOutRange,
            Members loginMember, Pageable pageable);

    /**
     *
     * @param memberId : 변경하려는 멤버 아이디
     * @param attendanceId : 변경하려는 멤버의 근태 아이디
     * @param loginMember : 로그인 멤버
     * @param requestDto : 입력받은 근태 상태, 변경하는 이유
     * @return 변경 상황 적용
     */
    AttendancesResponseDto.UpdateAttendancesStatusDto updateOneStatusForAdmin(
            Long memberId, Long attendanceId, Members loginMember,
            AttendancesRequestDto.UpdateAttendanceStatusDto requestDto);

    /**
     *
     * @param loginMember : 로그인 멤버
     * @param requestDto : 특정한 날에 특정한 사유(외근, 긴급한 휴가)로 근태 기록을 생성하는 DTO
     * @return 관리자가 생성한 특별한 근태 기록
     */
    AttendancesResponseDto.CreateOneDto createOneForAdmin(
            AttendancesRequestDto.CreateOneDto requestDto, Members loginMember);

    /**
     *
     * @param allMemberIdList : 모든 멤버의 id 리스트
     */
    void delete(List<Long> allMemberIdList);
}
