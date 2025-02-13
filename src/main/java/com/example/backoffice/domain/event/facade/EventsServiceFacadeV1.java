package com.example.backoffice.domain.event.facade;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.exception.EventsCustomException;
import com.example.backoffice.domain.event.exception.EventsExceptionCode;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventsServiceFacadeV1 {

    /**
     * Events Service Facade V1
     * 기본 규칙:
     * - 특별히 명시되지 않은 경우, 모든 매개변수는 필수입니다.
     * - 선택적인 매개변수는 "선택 사항" 또는 "null 가능"으로 명시됩니다.
     */

    /**
     * 부서 일정 생성
     * @param department : 생성하려는 일정의 부서
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link EventsRequestDto.CreateOneDepartmentTypeDto}
     * @param files : 첨부할 파일 (선택 사항, null 가능)
     * @return {@link EventsResponseDto.CreateOneDepartmentTypeDto}
     * 부서 일정 생성 응답 DTO
     */
    EventsResponseDto.CreateOneDepartmentTypeDto createOneDepartmentType(
            String department, Members loginMember,
            EventsRequestDto.CreateOneDepartmentTypeDto requestDto,
            List<MultipartFile> files);

    /**
     * 부서 한 달 일정 조회
     * @param department : 조회하려는 일정의 부서
     * @param year : 조회하고 싶은 년도
     * @param month : 조회하고 싶은 월
     * @param loginMember : 로그인 멤버
     * @return {@link EventsResponseDto.ReadOneDepartmentTypeDto}
     * 부서 한 달 일정 조회 응답 DTO
     * @throws EventsCustomException
     * {@link EventsExceptionCode#NO_PERMISSION_TO_READ_EVENT}
     * 로그인한 사용자가 조회하려는 일정의 아이디와 일치하지 않는 경우
     */
    List<EventsResponseDto.ReadOneDepartmentTypeDto> readForDepartmentMonthEvent(
        String department, Long year, Long month, Members loginMember);

    /**
     * 부서 일정 하나 수정
     * @param department : 수정하려는 일정의 부서
     * @param eventId : 수정할 일정 아이디
     * @param loginMember : 로그인 멤버
     * @param requestDto {@link EventsRequestDto.UpdateOneDepartmentTypeDto}
     * @param files : 첨부하려는 파일 (선택 사항, null 가능)
     * @return {@link EventsResponseDto.UpdateOneDepartmentTypeDto}
     * 부서 일정 하나 수정 응답 DTO
     * @throws EventsCustomException
     * {@link EventsExceptionCode#NO_PERMISSION_TO_READ_EVENT}
     * 로그인한 사용자가 조회하려는 일정의 아이디와 일치하지 않는 경우
     */
    EventsResponseDto.UpdateOneDepartmentTypeDto updateOneDepartmentType(
            String department, Long eventId, Members loginMember,
            EventsRequestDto.UpdateOneDepartmentTypeDto requestDto,
            List<MultipartFile> files);

    /**
     * 부서 일정 하나 삭제
     * @param department : 삭제하려는 일정의 부서
     * @param eventId : 삭제할 일정 아이디
     * @param loginMember : 로그인 멤버
     * @throws EventsCustomException
     * {@link EventsExceptionCode#NO_PERMISSION_TO_DELETE_EVENT}
     * 로그인한 멤버가 삭제하려는 일정을 삭제할 수 없는 권한일 경우
     * 일정을 만든 사람 또는 직위가 사장인 경우만 삭제가 가능
     */
    void deleteOneDepartmentType(String department, Long eventId, Members loginMember);

    /**
     * 멤버 일정 조회
     * @param memberId : 해당하는 멤버 아이디
     * @param year : 조회하려는 년도
     * @param month : 조회하려는 달
     * @param loginMember : 로그인 멤버
     * @return {@link EventsResponseDto.ReadOnePersonalScheduleDto}
     * 멤버 일정 조회 응답 DTO
     * Vacation, Event의 일부 정보가 둘 다 들어가 있는 응답 DTO
     */
    List<EventsResponseDto.ReadOnePersonalScheduleDto> readMonthlyPersonalSchedule(
            Long memberId, Long year, Long month, Members loginMember);

    /**
     * 멤버 일정 하루 조회
     * @param memberId : 조회할 멤버 아이디
     * @param year : 조회할 년도
     * @param month : 조회할 월
     * @param day : 조회할 일
     * @param loginMember : 로그인 멤버
     * @return {@link EventsResponseDto.ReadOnePersonalScheduleDto}
     * 멤버 일정 하루 조회 응답 DTO
     * Vacation, Event 도메인의 정보가 하나에 들어가 있음.
     * @throws EventsCustomException
     * {@link EventsExceptionCode#NO_PERMISSION_TO_READ_EVENT}
     * 로그인한 사용자가 조회하려는 일정의 아이디와 일치하지 않는 경우
     */
    List<EventsResponseDto.ReadOnePersonalScheduleDto> readDailyPersonalSchedule(
            Long memberId, Long year, Long month, Long day, Members loginMember);

}
