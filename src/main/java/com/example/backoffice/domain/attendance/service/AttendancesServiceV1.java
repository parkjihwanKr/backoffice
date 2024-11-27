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
     * 스케줄러를 통해 모든 멤버의 출석 기록을 자동 생성
     *
     * @param isWeekDay 평일 여부(true: 평일, false: 휴일),
     *                  캐싱 데이터와 스케줄러를 이용
     */
    void create(Boolean isWeekDay);

    /**
     * 출근 시간 업데이트
     *
     * @param attendanceId 근태 고유 ID
     * @param requestDto 출근 시간 업데이트 요청 DTO
     * @param loginMember 로그인 사용자
     * @return 업데이트된 출근 정보
     * @throws com.example.backoffice.domain.attendance.exception.AttendancesCustomException 근태 기록이 존재하지 않는 경우
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 로그인 사용자와 소유자가 일치하지 않는 경우
     * @throws com.example.backoffice.global.exception.DateUtilException 유효하지 않은 시간 데이터가 전달된 경우
     */
    AttendancesResponseDto.UpdateCheckInTimeDto updateCheckInTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            Members loginMember);

    /**
     * 퇴근 시간 및 상태 업데이트
     *
     * @param attendanceId 근태 고유 ID
     * @param requestDto 퇴근 시간 및 상태 업데이트 요청 DTO
     * @param loginMember 로그인 사용자
     * @return 업데이트된 퇴근 정보
     * @throws com.example.backoffice.domain.attendance.exception.AttendancesCustomException 근태 기록이 존재하지 않는 경우
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 로그인 사용자와 소유자가 일치하지 않는 경우
     * @throws com.example.backoffice.global.exception.DateUtilException 유효하지 않은 시간 데이터가 전달된 경우
     */
    AttendancesResponseDto.UpdateCheckOutTimeDto updateCheckOutTime(
            Long attendanceId,
            AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            Members loginMember);

    /**
     * 특정 멤버의 년/월별 근태 기록 조회
     *
     * @param memberId 멤버 ID
     * @param year 조회할 년도
     * @param month 조회할 월
     * @param attendanceStatus 필터링할 근태 상태 (optional)
     * @param loginMember 로그인 사용자
     * @return 멤버의 근태 정보 리스트
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 로그인 사용자와 멤버가 일치하지 않는 경우
     * @throws com.example.backoffice.global.exception.DateUtilException 유효하지 않은 년/월 데이터가 전달된 경우
     */
    List<AttendancesResponseDto.ReadOneDto> readFiltered(
            Long memberId, Long year, Long month,
            String attendanceStatus, Members loginMember);

    /**
     * 특정 근태 기록 조회
     *
     * @param attendanceId 근태 고유 ID
     * @param loginMember 로그인 사용자
     * @return 근태 기록 정보
     * @throws com.example.backoffice.domain.attendance.exception.AttendancesCustomException 근태 기록이 존재하지 않는 경우
     * @throws com.example.backoffice.domain.attendance.exception.AttendancesCustomException 접근 권한이 없는 경우
     */
    AttendancesResponseDto.ReadOneDto readOne(
            Long attendanceId, Members loginMember);

    /**
     * 관리자를 위한 근태 관리 페이지 조회
     *
     * @param memberName 필터링할 멤버 이름 (optional)
     * @param attendanceStatus 필터링할 근태 상태 (optional)
     * @param checkInRange 필터링할 출근 시간 범위 (optional)
     * @param checkOutRange 필터링할 퇴근 시간 범위 (optional)
     * @param loginMember 로그인 사용자
     * @param pageable 페이징 정보
     * @return 필터링된 근태 관리 페이지
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 관리 권한이 없는 경우
     * @throws com.example.backoffice.global.exception.DateUtilException 유효하지 않은 날짜 범위가 전달된 경우
     */
    Page<AttendancesResponseDto.ReadOneDto> readForAdmin(
            String memberName, String attendanceStatus,
            DateRange checkInRange, DateRange checkOutRange,
            Members loginMember, Pageable pageable);

    /**
     * 관리자가 특정 멤버의 근태 상태를 업데이트
     *
     * @param memberId 멤버 ID
     * @param attendanceId 근태 고유 ID
     * @param loginMember 로그인 사용자
     * @param requestDto 상태 업데이트 요청 DTO
     * @return 업데이트된 근태 상태 정보
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 관리 권한이 없는 경우
     * @throws com.example.backoffice.domain.attendance.exception.AttendancesCustomException 근태 상태가 동일한 경우
     */
    AttendancesResponseDto.UpdateAttendancesStatusDto updateOneStatusForAdmin(
            Long memberId, Long attendanceId, Members loginMember,
            AttendancesRequestDto.UpdateAttendanceStatusDto requestDto);

    /**
     * 관리자가 특정 날의 근태 기록을 생성
     *
     * @param requestDto 근태 기록 생성 요청 DTO
     * @param loginMember 로그인 사용자
     * @return 생성된 근태 기록 정보
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 관리 권한이 없는 경우
     * @throws com.example.backoffice.global.exception.DateUtilException 유효하지 않은 날짜 범위가 전달된 경우
     */
    AttendancesResponseDto.CreateOneDto createOneForAdmin(
            AttendancesRequestDto.CreateOneDto requestDto, Members loginMember);

    /**
     * 오래된 근태 기록을 삭제
     *
     * @param allMemberIdList 삭제할 멤버 ID 리스트
     */
    void delete(List<Long> allMemberIdList);

    /**
     * 관리자가 전산 오류로 인한 근태 기록을 삭제
     *
     * @param requestDto 삭제 요청 DTO
     * @param loginMember 로그인 사용자
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 관리 권한이 없는 경우
     */
    void deleteForAdmin(
            AttendancesRequestDto.DeleteForAdminDto requestDto, Members loginMember);

    /**
     * 관리자가 전산 오류로 인해 근태 기록을 수동 생성
     *
     * @param requestDto 근태 기록 수동 생성 요청 DTO
     * @param loginMember 로그인 사용자
     * @return 생성된 근태 기록 정보
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 관리 권한이 없는 경우
     * @throws com.example.backoffice.domain.attendance.exception.AttendancesCustomException 중복된 근태 기록이 존재하는 경우
     */
    AttendancesResponseDto.CreateOneDto createOneManuallyForAdmin(
            AttendancesRequestDto.CreateOneManuallyForAdminDto requestDto, Members loginMember);

    /**
     * 모든 멤버의 월간 근태 기록 조회
     *
     * @param memberName 필터링할 멤버 이름 (optional)
     * @param department 필터링할 부서 이름 (optional)
     * @param year 조회할 년도
     * @param month 조회할 월
     * @param pageable 페이징 정보
     * @param loginMember 로그인 사용자
     * @return 필터링된 월간 근태 기록 페이지
     * @throws com.example.backoffice.domain.member.exception.MembersCustomException 관리 권한이 없는 경우
     * @throws com.example.backoffice.global.exception.DateUtilException 유효하지 않은 날짜 범위가 전달된 경우
     */
    Page<AttendancesResponseDto.ReadMonthlyDto> readFilteredByMonthlyForAdmin(
            String memberName, String department, Long year, Long month,
            Pageable pageable, Members loginMember);
}
