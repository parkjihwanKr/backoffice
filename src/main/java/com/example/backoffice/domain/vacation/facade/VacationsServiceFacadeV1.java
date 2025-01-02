package com.example.backoffice.domain.vacation.facade;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsExceptionCode;
import com.example.backoffice.global.exception.DateUtilException;
import com.example.backoffice.global.exception.GlobalExceptionCode;


import java.util.List;

/**
 * VacationsServiceFacadeV1
 * 휴가 관리 기능을 제공하는 인터페이스로, 휴가 신청, 수정, 삭제 및 관리자 기능을 포함합니다.
 *
 * <h3>휴가 신청 기간</h3>
 * <ul>
 *     <li>매달 2주차 월요일부터 3주차 금요일까지 신청 가능합니다.</li>
 *     <li>관리자가 특별한 경우 수동으로 해당 휴가 기간을 조정할 수 있습니다.</li>
 * </ul>
 *
 * <h3>휴가 공통 규칙</h3>
 * <ul>
 *     <li>모든 휴가는 1일 또는 최대 15일까지 사용할 수 있습니다.</li>
 *     <li>휴가의 시작일은 토요일이나 일요일일 수 없습니다.</li>
 * </ul>
 *
 * <h3>휴가 생성</h3>
 * <ul>
 *     <li>정해진 신청 기간 내에 '연가'를 사용할 수 있습니다.</li>
 *     <li>'연가'에 해당하는 휴가를 신청할 때, 출타율이 30%가 넘으면 제한될 수 있습니다.</li>
 *     <li>'긴급 휴가'나 '병가'는 신청 기간과 무관하며, 긴급 휴가는 잔여 휴가 일수를 소모하지 않습니다.</li>
 * </ul>
 *
 * <h3>휴가 수정 및 삭제</h3>
 * <ul>
 *     <li>승인된 휴가는 관리자와 협의하여 수정/삭제할 수 있습니다.</li>
 *     <li>승인되지 않은 휴가는 멤버가 직접 수정/삭제할 수 있습니다.</li>
 * </ul>
 *
 * <h3>휴가 삭제</h3>
 * <ul>
 *     <li>관리자는 멤버의 휴가를 삭제할 수 있습니다.</li>
 * </ul>
 *
 */
public interface VacationsServiceFacadeV1 {

    /**
     * 관리자에 의해 수동으로 휴가 신청 기간을 변경
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link VacationsRequestDto.UpdatePeriodDto}
     * 휴가 신청 기간을 변경 요청 DTO
     * @return {@link VacationsResponseDto.UpdatePeriodDto}
     * 관리자에 의해 수동으로 휴가 신청 기간을 변경 응답 DTO
     * @throws VacationsCustomException {@link VacationsExceptionCode#NO_PERMISSION_TO_UPDATE_VACATION}
     * HR MANAGER 또는 CEO가 아닌 권한이 해당 휴가 신청 기간을 변경하였을 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#INVALID_VACATION_PERIOD}
     * 현재 달의 휴가 신청 기간을 변경하지 않는 경우
     * ex) 2024-12-10에 2023-01월의 휴가 신청 기간을 변경하려는 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#INVALID_START_DATE}
     * 새로운 휴가 신청 기간의 시작일이 내일 이전인 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#END_DATE_BEFORE_START_DATE}
     * 새로운 휴가 신청 기간의 시작일이 새로운 휴가 신청 기간의 종료일보다 느린 경우
     */
    VacationsResponseDto.UpdatePeriodDto updatePeriodByAdmin(
            Members loginMember, VacationsRequestDto.UpdatePeriodDto requestDto);

    /**
     * 휴가 생성
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link VacationsRequestDto.CreateOneDto}
     * 휴가 생성 요청 DTO
     * @return {@link VacationsResponseDto.CreateOneDto}
     * 휴가 생성 응답 DTO
     * @throws VacationsCustomException {@link VacationsExceptionCode#END_DATE_BEFORE_START_DATE}
     * 요청한 휴가 시작일이 종료일보다 느린 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#INVALID_START_DATE}
     * 요청한 휴가 시작일이 오늘 이전인 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#INVALID_START_DATE_WEEKEND}
     * 요청한 휴가 시작일이 토요일 또는 일요일인 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#INVALID_END_DATE_WEEKEND}
     * 요청한 휴가 종료일이 토요일 또는 일요일인 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#INSUFFICIENT_VACATION_DAYS}
     * 로그인 멤버의 잔여 휴가일이 충분하지 않은 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#OVERLAPPING_VACATION_DATES}
     * 기존에 신청한 휴가와 새롭게 신청한 기간과 겹치는 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#EXCEEDS_VACATION_RATE_LIMIT}
     * 멤버가 '연가'를 사용하는 휴가 기간에 다른 멤버들의 휴가 출타율이 0.3이 넘는 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#NOT_FOUND_VACATION_TYPE}
     * 서버에 존재하지 않는 휴가 타입을 입력했을 경우(연가, 병가, 긴급한 휴가)
     * @throws VacationsCustomException {@link VacationsExceptionCode#DO_NOT_NEED_URGENT}
     * 멤버가 '연가' 요청을 한 경우인데, '긴급함' 표시에 체크한 경우
     * @throws VacationsCustomException {@link VacationsExceptionCode#NEED_URGENT}
     * 멤버가 '긴급한 휴가' 또는 '병가' 요청을 한 경우인데, '긴급함' 표시에 체크를 안 한 경우
     */
    VacationsResponseDto.CreateOneDto createOneByMember(
            Members loginMember, VacationsRequestDto.CreateOneDto requestDto);

    /**
     * 특정 휴가 하루를 조회
     * @param vacationId  휴가 아이디
     * @param loginMember 로그인 멤버
     * @return {@link VacationsResponseDto.ReadDayDto}
     * 특정 휴가 하루를 조회 응답 DTO
     * @throws VacationsCustomException {@link VacationsExceptionCode#NO_PERMISSION_TO_READ_VACATION}
     * 로그인한 멤버와 해당 휴가를 만든 사람이 아닌 경우
     */
    VacationsResponseDto.ReadDayDto readDayByMember(
            Long vacationId, Members loginMember);

    /**
     * 휴가 하나 수정
     * @param vacationId  수정할 휴가 아이디
     * @param loginMember 로그인 멤버
     * @param requestDto  수정 요청 DTO
     * @return 수정된 휴가 정보
     */
    VacationsResponseDto.UpdateOneDto updateOneByMember(
            Long vacationId, Members loginMember,
            VacationsRequestDto.UpdateOneDto requestDto);

    /**
     * 특정 권한을 가진 관리자가 특정 멤버의 휴가를 수정
     * @param vacationId  수정할 휴가 아이디
     * @param loginMember 로그인 멤버
     * @return {@link VacationsResponseDto.UpdateOneByAdminDto}
     * 특정 권한을 가진 관리자가 휴가를 수정 응답 DTO
     * @throws VacationsCustomException {@link VacationsExceptionCode#NO_PERMISSION_TO_UPDATE_VACATION}
     * 로그인 멤버가 해당 휴가를 수정할 수 없는 경우
     * ex) HR_MANAGER || CEO만 해당 휴가를 수정 가능
     */
    VacationsResponseDto.UpdateOneByAdminDto updateOneByAdmin(
            Long vacationId, Members loginMember);

    /**
     * 휴가 하나 삭제
     * @param vacationId  삭제할 휴가 ID
     * @param loginMember 로그인 멤버(모든 멤버)
     * @throws VacationsCustomException {@link VacationsExceptionCode#NO_PERMISSION_TO_DELETE_VACATION}
     * 휴가를 삭제할 수 없는 멤버가 삭제할 경우
     * ex) 휴가를 생성한 멤버만 가능
     * @throws VacationsCustomException {@link VacationsExceptionCode#NOT_ACCEPTED_DELETE_VACATION}
     * 휴가가 승인된 상태에서 휴가를 생성한 멤버가 삭제하려는 경우
     */
    void deleteOneByMember(Long vacationId, Members loginMember);

    /**
     * HR 관리자가 특정 조건에 따라 휴가 목록을 조회
     * @param year       조회 연도
     * @param month      조회 월
     * @param isAccepted 승인 여부
     * @param urgent     긴급 여부
     * @param department 조회할 부서
     * @param loginMember 로그인한 관리자 멤버
     * @return {@link List<VacationsResponseDto.ReadMonthDto>}
     * HR 관리자가 특정 조건에 따라 휴가 목록을 조회 응답 DTO
     * @throws DateUtilException {@link GlobalExceptionCode#INVALID_YEAR}
     * 입력 받은 년도가 음수 일 때
     * @throws DateUtilException {@link GlobalExceptionCode#INVALID_MONTH}
     * 입력 받은 월이 0이하거나 13이상일 경우
     */

    List<VacationsResponseDto.ReadMonthDto> readByHrManager(
            Long year, Long month, Boolean isAccepted, Boolean urgent,
            String department, Members loginMember);

    /**
     * 관리자가 멤버의 휴가를 삭제
     * @param vacationId 삭제할 휴가 아이디
     * @param requestDto {@link VacationsRequestDto.DeleteOneByAdminDto} 삭제 요청 DTO
     * @param loginMember 로그인 멤버
     */
    void deleteOneByHrManager(
            Long vacationId, VacationsRequestDto.DeleteOneByAdminDto requestDto,
            Members loginMember);
}
