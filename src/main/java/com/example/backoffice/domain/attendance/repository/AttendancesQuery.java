package com.example.backoffice.domain.attendance.repository;

import com.example.backoffice.domain.attendance.entity.AttendanceStatus;
import com.example.backoffice.domain.attendance.entity.Attendances;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendancesQuery {

    /**
     * 멤버 아이디가 동일하고 시작일과 마지막일 사이에 존재하는 근태 기록 리스트 조회
     * @param memberId : 멤버 아이디
     * @param startDate : year:month:01T00:00:00
     * @param endDate : year:month:monthOfLastDayT:23:59:59
     * @return : 필터링된 근태 기록 리스트
     */
    List<Attendances> findFilteredByMember(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 아래의 파라미터가 필터링된 근태 기록 페이지
     * @param foundMemberId : 찾으려는 멤버 아이디
     * @param attendanceStatus : 근태 상태
     * @param checkInStartTime : 출근 시간 시작 범위
     * @param checkInEndTime : 출근 시간 끝 범위
     * @param checkOutStartTime : 퇴근 시간 시작 범위
     * @param checkOutEndTime : 퇴근 시간 끝 범위
     * @param pageable : createdAt 오름차순, size = 7
     * @return 필터링된 근태 기록 페이지
     */
    Page<Attendances> findFilteredForAdmin(
            Long foundMemberId, AttendanceStatus attendanceStatus,
            LocalDateTime checkInStartTime, LocalDateTime checkInEndTime,
            LocalDateTime checkOutStartTime, LocalDateTime checkOutEndTime,
            Pageable pageable);

    /**
     * 제작년의 근태 기록을 스케줄러를 통한 삭제
     * @param startOfDeletion : before2Year:01:01T00:00:00
     * @param endOfDeletion : before2Year:12:31T23:59:59
     */
    void deleteBeforeTwoYear(
            LocalDateTime startOfDeletion,
            LocalDateTime endOfDeletion);

    /**
     * 멤버 아이디와 생성일자를 통한 근태 기록 조회
     * @param memberId : 멤버 아이디
     * @param createdDate : 생성 일자
     * @return : 해당 파라미터에 맞는 Attendances
     */
    Optional<Attendances> findByMemberIdAndCreatedDate(
            Long memberId, LocalDate createdDate);

    /**
     * 근태 기록을 수동 저장 또는 변경
     * ** 해당하는 근태 기록이 스케줄러에 의해 만들어지지 않았을 때
     * ** 해당하는 근태 기록의 전산상 오류일 때(기록이 잘못되었을 경우)
     * @param memberId : 저장 또는 수정할 멤버 아이디
     * @param customCreatedAt : 근태 기록의 생성/수정하려는 해당 날짜
     * @param attendance : 생성/수정하려는 근태 기록
     */
    void saveManually(
            Long memberId, LocalDateTime customCreatedAt,
            Attendances attendance);

    /**
     * 관리자가 해당하는 파라미터를 필터링한 근태 기록 조회
     * @param memberIdList : 조회하려는 멤버 아이디
     * @param customStartDay : 해당하는 시작 날짜
     * @param customEndDay : 해당하는 마지막 날짜
     * @return : 필터링된 근태 기록 리스트
     */
    List<Attendances> findAllFilteredByAdmin(
            List<Long> memberIdList, LocalDateTime customStartDay,
            LocalDateTime customEndDay);

    /**
     * 관리자가 해당하는 파라미터를 필터링한 근태 기록 페이지 조회
     * @param memberIdList : 조회하려는 멤버 아이디
     * @param yearMonthStartDay : year:01:01T00:00:00
     * @param yearMonthEndDay : year:12:31T23:59:59
     * @param pageable 생성일자 오름차순 정렬, size = 20
     * @return : 필터링된 근태 기록 페이지 조회
     */
    Page<Attendances> findAllFilteredByAdmin(
            List<Long> memberIdList, LocalDateTime yearMonthStartDay,
            LocalDateTime yearMonthEndDay, Pageable pageable);
}
