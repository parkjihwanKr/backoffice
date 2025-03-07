package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.global.scheduler.ScheduledEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MembersServiceV1 {

    /**
     * 회원 가입
     * @param member 회원 가입 정보
     */
    void signup(Members member);

    /**
     * 멤버 아이디를 사용할 수 있는지 체크
     * @param requestedMemberName : requestDTO를 통해 받아온 멤버 아이디
     * @throws MembersCustomException {@link MembersExceptionCode#EXISTS_MEMBER}
     * 존재하는 멤버인 경우
     * @throws MembersCustomException {@link MembersExceptionCode#INVALID_MEMBER_NAME}
     * 특수한 정규식에 부합하지 않는 멤버 아이디를 요청한 경우
     */
    void checkAvailableMemberName(String requestedMemberName);

    /**
     * 멤버 아이디를 통한 멤버 조회
     * @param memberId 조회할 멤버의 ID
     * @return 조회된 멤버
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    Members findById(Long memberId);

    /**
     * 두 멤버가 동일 비교(** 로그인 하지 않은 멤버를 비교)
     * @param fromMemberId 기준 멤버 ID
     * @param toMemberId 비교할 멤버 ID
     * @return 비교 대상 멤버
     * @throws MembersCustomException {@link MembersExceptionCode#MATCHED_LOGIN_MEMBER}
     * 두 멤버가 동일한 경우
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    Members checkDifferentMember(Long fromMemberId, Long toMemberId);

    /**
     * 두 멤버가 동일한지 비교 (** 로그인한 멤버를 비교)
     * @param loginMember : 로그인 멤버
     * @param memberId : 비교할 멤버 아이디
     * @return 비교 대상 멤버
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_MATCHED_INFO}
     */
    Members matchLoginMember(Members loginMember, Long memberId);

    /**
     * 중복되는 멤버인지 확인
     * @param requestDto : 회원가입 진행을 위한 요청 DTO
     */
    void checkDuplicatedMember(MembersRequestDto.CreateOneDto requestDto);

    /**
     * 멤버 ID가 존재 확인
     * @param memberId 확인할 멤버 ID
     * @return 존재 여부
     */
    Boolean existsById(Long memberId);

    /**
     * 멤버를 저장
     * @param member 저장할 멤버
     * @return 저장된 멤버
     */
    Members save(Members member);

    /**
     * 멤버를 삭제
     * @param memberId 삭제할 멤버 ID
     */
    void deleteById(Long memberId);

    /**
     * 멤버 아이디 리스트를 통한 해당 멤버 리스트 조회
     * @param memberIdList 조회할 멤버 ID 리스트
     * @return 조회된 멤버 리스트
     */
    List<Members> findAllById(List<Long> memberIdList);

    /**
     * HR Manager 조회
     * @return HR Manager
     */
    Members findHRManager();

    /**
     * Audit Manager || CEO 조회
     * @param memberId : 조회할 멤버 아이디
     * @return Audit Manager || CEO
     * @throws MembersCustomException {@link MembersExceptionCode#RESTRICTED_ACCESS_MEMBER}
     * 접근 불가능한 멤버
     */
    Members findAuditManagerOrCeo(Long memberId);

    /**
     * 요청받은 부서, 멤버를 제외한 리스트를 조회
     * @param excludedDepartmentList 제외할 부서 리스트
     * @param excludedIdList 제외할 멤버 아이디 리스트
     * @return 제외한 부서, 멤버를 제외한 부서, 멤버 이름 맵
     */
    Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    /**
     * 멤버 이름을 통한 CEO 조회
     * @param memberName : 멤버 이름
     * @return CEO
     * @throws MembersCustomException {@link MembersExceptionCode#RESTRICTED_ACCESS_MEMBER}
     * 접근 불가능한 멤버
     */
    Members findCeoByMemberName(String memberName);

    /**
     * 직책을 통한 멤버 조회
     * @param position : 조회할 직책
     * @return 직책에 맞는 멤버
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    Members findByPosition(MemberPosition position);

    /**
     * 스케줄러 타입에 따른 멤버 하나의 휴가 일수 수정
     * @param scheduledEventType : 연간, 월간 스케줄러 구분하는 ENUM
     */
    void updateOneForRemainingVacationDays(ScheduledEventType scheduledEventType);

    /**
     * 멤버의 총 개수를 반환
     * @return 멤버 총 수
     */
    Long findMemberTotalCount();

    /**
     * 멤버 이름으로 멤버를 조회
     * @param memberName 조회할 멤버 이름
     * @return 조회된 멤버
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    Members findByMemberName(String memberName);

    /**
     * 모든 멤버 리스트 조회
     * @return 멤버 리스트
     */
    List<Members> findAll();

    /**
     * 요청 받은 부서에 속한 모든 멤버를 조회
     * @param department 부서 정보
     * @return 멤버 리스트
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    List<Members> findAllByDepartment(MemberDepartment department);

    /**
     * 요청 받은 부서에 속한 모든 멤버를 조회
     * @param pageable : 페이징 초기 세팅
     * @param position : 조회할 직책
     * @return 해당하는 직책의 멤버 페이지
     */
    Page<Members> findAllByPosition(Pageable pageable, MemberPosition position);

    /**
     * 요청 받은 부서에 속한 모든 멤버를 조회
     * @param pageable : 페이징 초기 세팅
     * @param department : 조회할 부서
     * @return 해당하는 부서의 멤버 페이지
     */
    Page<Members> findAllByDepartment(Pageable pageable, MemberDepartment department);

    /**
     * 로그인 멤버를 제외한 모든 멤버를 조회
     * @param exceptMemberId 제외할 멤버 ID
     * @return 멤버 리스트
     */
    List<Members> findAllExceptLoginMember(Long exceptMemberId);

    /**
     * 휴가 일수를 추가
     * @param onVacationMember 휴가 대상 멤버
     * @param plusVacationDays 추가할 휴가 일수
     */
    void addVacationDays(Members onVacationMember, int plusVacationDays);

    /**
     * 휴가 일수를 차감
     * @param onVacationMember 휴가 대상 멤버
     * @param minusVacationDays 차감할 휴가 일수
     */
    void minusVacationDays(Members onVacationMember, int minusVacationDays);

    /**
     * HR 매니저 또는 CEO를 조회합니다.
     * @param member 로그인한 멤버
     * @return HR 매니저 또는 CEO
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    Members findHRManagerOrCEO(Members member);

    /**
     * 부서 정보를 반환
     * @param department 부서명
     * @return 부서 정보
     */
    MemberDepartment findDepartment(String department);

    /**
     * 페이징 처리를 통해 모든 멤버를 조회
     * @param pageable 페이지 정보
     * @return 멤버 페이지
     */
    Page<Members> findAll(Pageable pageable);

    /**
     * 요청 받은 부서와 직위에 속한 멤버를 조회
     * @param department 부서 정보
     * @param position 직위 정보
     * @param pageable 페이지 정보
     * @return 멤버 페이지
     */
    Page<Members> findAllByDepartmentAndPosition(
            MemberDepartment department, MemberPosition position, Pageable pageable);

    /**
     * 멤버 이름이 존재하는지 확인합니다.
     *
     * @param memberName 확인할 이름
     * @return 존재 여부
     */
    Boolean isExistMemberName(String memberName);

    /**
     * 멤버의 휴가 상태를 '휴가 중'-> '근무 중'으로 수정
     * @param onVacationMemberId 대상 멤버 ID
     */
    void updateOneForOnVacationFalse(Long onVacationMemberId);

    /**
     * 멤버의 휴가 상태를 '근무 중'-> '휴가 중'으로 수정
     * @param onVacationMemberId 대상 멤버 ID
     */
    void updateOneForOnVacationTrue(Long onVacationMemberId);

    /**
     * 멤버 이름을 통해 멤버 리스트 조회
     * @param memberName : 조회할 멤버 이름
     * @return 멤버 이름과 일치하는 멤버 리스트
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    List<Members> findAllByMemberName(String memberName);

    /**
     * 부서와 멤버 이름을 통해 멤버 리스트 조회
     * @param department : 조회할 부서
     * @param memberName : 조회할 멤버 이름
     * @return 부서와 멤버 이름과 일치하는 멤버 리스트
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    List<Members> findAllByDepartment(MemberDepartment department, String memberName);

    /**
     * 직책이 매니저 이거나 사장인지 확인
     * @param loginMember : 로그인 멤버
     * @return 직책이 매니저 이거나 사장인지 확인 여부에 따라 true, false
     */
    boolean isManagerOrCeo(Members loginMember);

    /**
     * 멤버 아이디와 부서를 통해 멤버 조회
     * @param memberId : 조회할 멤버 아이디
     * @param department : 조회할 부서
     * @return : 해당 부서와 아이디에 맞는 멤버
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    Members findByIdAndDepartment(Long memberId, MemberDepartment department);

    /**
     * IT MANAGER 조회
     * @return IT MANAGER || null
     */
    Members findItManager();

    /**
     * CEO 조회
     * @return CEO || null
     */
    Members findCeo();

    /**
     * password와 passwordConfirm이 일치하는지 확인
     * @param password : 요청 받은 패스워드
     * @param passwordConfirm : 요청 받은 패스워드 확인
     */
    void checkPassword(String password, String passwordConfirm);

    /**
     * 요청받은 패스워드를 BCrytPassword 인코딩
     * @param password : 요청 받은 패스워드
     * @return BCrytPassword
     */
    String encodePassword(String password);

    /**
     * 자기 소개 길이가 긴지 체크
     * @param requestedIntroduction : 요청 받은 자기소개
     * @throws MembersCustomException {@link MembersExceptionCode#MAX_LENGTH_500}
     * 자기 소개의 길이가 길 때
     */
    void checkIntroductionMaxLength(String requestedIntroduction);

    /**
     * 요청 받은 멤버 아이디와 일치하는 멤버 아이디가 일치하는지 확인
     * @param loginMemberName : 로그인 멤버의 멤버 아이디
     * @param memberName : 요청 받은 멤버 아이디
     */
    void matchedMemberName(String memberName, String loginMemberName);

}
