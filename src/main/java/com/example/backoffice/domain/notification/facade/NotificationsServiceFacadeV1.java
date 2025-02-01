package com.example.backoffice.domain.notification.facade;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.exception.NotificationsCustomException;
import com.example.backoffice.domain.notification.exception.NotificationsExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationsServiceFacadeV1 {

    /**
     * 특정 상황에 맞는 알림 생성
     * 모든 상황에 api 요청에 알림이 가는 것이 아닌 특정 상황에 따른 알림 생성
     * @param notificationData {@link NotificationData}
     * 도메인 이름
     * @param domainType
     * 각각의 역할에 맞는 도메인 형태의 알림을 Enum 형태로 저장
     */
    void createOne(NotificationData notificationData, NotificationType domainType);

    /**
     * 알림 하나 조회
     * @param memberId : 해당 알림의 멤버의 아이디
     * @param notificationId : 해당 알림 아이디
     * @param loginMember : 로그인 멤버
     * @return {@link NotificationsResponseDto.ReadOneDto}
     * 해당하는 멤버의 알림 하나 조회
     */
    NotificationsResponseDto.ReadOneDto readOne(
            Long memberId, String notificationId, Members loginMember);

    /**
     * CEO가 수동으로 모든 멤버에게 알림 생성
     * @param memberName : CEO 멤버 이름
     * @param requestDto {@link NotificationsRequestDto.CreateByAdminDto}
     * 모든 멤버에게 보내고 싶은 메세지
     * @throws NotificationsCustomException {@link NotificationsExceptionCode#RESTRICT_ACCESS}
     * CEO가 아닌 다른 인원이 알림을 생성하려는 경우
     * front에서 관리자 시스템 페이지를 접속 할 수 없으니, Manager 이상의 권한을 가진 멤버가 알림을 생성하는 경우
     */
    void createByAdmin(
            String memberName, NotificationsRequestDto.CreateByAdminDto requestDto);

    /**
     * 관리자에 의한 필터링된 알림 생성
     * @param memberName : 관리자 멤버 이름
     * @param requestDto {@link NotificationsRequestDto.CreateFilteredByAdminDto}
     * 특정 멤버 또는 특정 부서에게 보내는 알림 메세지 요청 DTO
     */
    void createFilteredByAdmin(
            String memberName,
            NotificationsRequestDto.CreateFilteredByAdminDto requestDto);

    /**
     * 알림 리스트를 페이지로 조회
     * @param memberId : 해당하는 멤버 아이디
     * @param loginMember : 로그인 멤버
     * @param pageable : 페이징
     * @return {@link Page<NotificationsResponseDto.ReadDto>}
     * 해당하는 알림 리스트를 페이징화한 응답 DTO
     */

    Page<NotificationsResponseDto.ReadDto> read(
            Long memberId, Members loginMember, Pageable pageable);

    /**
     * 모든 알림 리스트를 '읽음' 상태로 변경
     * @param memberId : 해당 멤버 아이디
     * @param loginMember : 로그인 멤버
     * @return {@link NotificationsResponseDto.ReadSummaryOneDto}
     * 모든 알림 리스트를 '읽음' 상태로 변경 응답 DTO
     */
    List<NotificationsResponseDto.ReadSummaryOneDto> changeIsReadTrue(Long memberId, Members loginMember);

    /**
     * 조건에 맞는 알림들을 삭제
     * @param memberId : 해당하는 멤버 아이디
     * @param requestDto {@link NotificationsRequestDto.DeleteDto}
     * 해당 알림 리스트 아이디를 삭제 요청 DTO
     * @param loginMember : 로그인 멤버
     * @return : 삭제된 알림 아이디 리스트
     */
    List<String> delete(
            Long memberId, NotificationsRequestDto.DeleteDto requestDto, Members loginMember);
}