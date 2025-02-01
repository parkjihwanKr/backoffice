package com.example.backoffice.domain.member.facade;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MembersServiceFacadeV1 {

    /**
     * 회원 가입
     * @param requestDto 회원가입에 필요한 생성 DTO
     * @return 생성된 멤버의 기본적인 정보
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_MATCHED_PASSWORD}
     * 비밀번호와 비밀번호 확인이 일치하지 않을 경우
     * @throws MembersCustomException {@link MembersExceptionCode#DUPLICATED_EMAIL}
     * 이미 등록된 이메일이 존재하는 경우
     * @throws MembersCustomException {@link MembersExceptionCode#DUPLICATED_ADDRESS}
     * 이미 등록된 주소가 존재하는 경우
     * @throws MembersCustomException {@link MembersExceptionCode#DUPLICATED_MEMBER_NAME}
     * 이미 등록된 이름이 존재하는 경우
     * @throws MembersCustomException {@link MembersExceptionCode#DUPLICATED_CONTACT}
     * 이미 등록된 연락처가 존재하는 경우
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_EXCEPTION_TYPE}
     * 예외 타입을 찾을 수 없는 경우
     * @
     */
    MembersResponseDto.CreateOneDto createOneForSignup(
            MembersRequestDto.CreateOneDto requestDto);

    /**
     * 멤버 이름(아이디)에 대한 중복 체크
     * @param memberName : 요청 받은 멤버 이름
     * @return 생성이 가능한지의 여부를 알려주는 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#EXISTS_MEMBER}
     * 이미 존재하는 멤버 이름일 경우
     * @throws MembersCustomException {@link MembersExceptionCode#INVALID_MEMBER_NAME}
     * 소문자 알파벳 또는 숫자인 문자가 8개 이상이거나 16이하가 아닌 경우
     */
    MembersResponseDto.ReadAvailableMemberNameDto checkAvailableMemberName(
            String memberName);

    /**
     * 멤버 하나 정보 조회
     * @param memberId : 조회하고 싶은 멤버 아이디
     * @param loginMember : 로그인 멤버 (자기 자신 || HR Manager || CEO)
     * @return 멤버의 상세보기 정보 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없을 경우
     * @throws MembersCustomException {@link MembersExceptionCode#RESTRICTED_ACCESS_MEMBER}
     * 접근 권한이 없을 경우
     */
    MembersResponseDto.ReadOneDetailsDto readOne(Long memberId, Members loginMember);

    /**
     * HR Manager가 모든 멤버의 조회 또는 필터링하여 조회
     * @param department : 조회하고 싶은 부서 (** 모든 부서는 null로 대체 가능)
     * @param position : 조회하고 싶은 직위 (** 모든 직위는 null로 대체 가능)
     * @param loginMember : 로그인 멤버(HR MANAGER || CEO)
     * @param pageable : 페이징
     * @return 멤버의 일부 정보(비밀번호 제외)를 페이지
     * @throws MembersCustomException {@link MembersExceptionCode#RESTRICTED_ACCESS_MEMBER}
     * 접근 권한이 없을 경우
     */
    Page<MembersResponseDto.ReadOneSummaryDto> readByAdmin(
            String department, String position,
            Members loginMember, Pageable pageable);

    /**
     * 자기 자신의 정보 수정
     * @param memberId : 수정하고 싶은 멤버 아이디(로그인 멤버)
     * @param loginMember : 로그인 멤버 (자기 자신)
     * @param requestDto : 수정하려고 하는 응답 정보 DTO
     * @return : 수정이 된 멤버 정보 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_MATCHED_MEMBER_NAME}
     * 멤버 이름이 일치하지 않을 경우
     * @throws MembersCustomException
     * 이메일이 중복될 경우
     * @throws MembersCustomException {@link MembersExceptionCode#DUPLICATED_CONTACT}
     * 연락처가 중복될 경우
     */
    MembersResponseDto.UpdateOneDto updateOne(
            Long memberId,  Members loginMember,
            MembersRequestDto.UpdateOneDto requestDto);

    /**
     * 멤버의 직책, 연봉을 수정
     * @param memberId : 수정할 멤버 아이디
     * @param loginMember : 로그인 멤버 (HR MANAGER || CEO)
     * @param requestDto : 변경하고자 하는 연봉, 직위, 직책에 대한 요청 DTO
     * @param multipartFile : 요청 자료
     * @return 수정된 멤버의 직책, 연봉 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#RESTRICTED_ACCESS_MEMBER}
     * 권한이 없을 경우
     */
    MembersResponseDto.UpdateOneForAttributeDto updateOneForAttributeByAdmin(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForAttributeDto requestDto,
            MultipartFile multipartFile);

    /**
     * 멤버의 프로필 사진을 수정
     * @param memberId : 수정할 멤버 아이디
     * @param loginMember : 로그인 멤버(자기 자신)
     * @param image : 변경하고자 하는 프로필 사진
     * @return 수정된 멤버의 프로필 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_MATCHED_IMAGE_FILE}
     * 이미지 파일 형식이 올바르지 않을 경우
     */
    MembersResponseDto.UpdateOneForProfileImageDto updateOneForProfileImage(
            Long memberId, Members loginMember, MultipartFile image);

    /**
     * 멤버의 프로필 사진 삭제
     * @param memberId : 삭제할 멤버 아이디
     * @param loginMember : 로그인 멤버(자기 자신)
     * @return  프로필 사진 삭제 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_BLANK_IMAGE_FILE}
     * 프로필 이미지가 비어 있는 경우
     */
    MembersResponseDto.DeleteOneForProfileImageDto deleteOneForProfileImage(
            Long memberId, Members loginMember);

    /**
     * 멤버 삭제
     * @param memberId : 삭제할 멤버 아이디
     * @param loginMember : 로그인 멤버(HR Manager || CEO)
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    void deleteOneByAdmin(Long memberId, Members loginMember);

    /**
     * 멤버의 연봉 수정
     * @param memberId : 수정할 멤버 아이디
     * @param loginMember : 로그인 멤버 (HR Manager || CEO)
     * @param requestDto : 연봉 수정 요청 DTO
     * @return 연봉 수정 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#RESTRICTED_ACCESS_MEMBER}
     * 권한 부족으로 수정할 수 없는 경우
     */
    MembersResponseDto.UpdateOneForSalaryDto updateOneForSalaryByAdmin(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForSalaryDto requestDto);

    /**
     * 멤버 한명의 휴가 리스트 조회
     * @param memberId : 조회할 멤버 아이디
     * @param loginMember : 로그인 멤버 (자기 자신 || HR Manager || CEO)
     * @return 멤버 하나의 휴가 리스트 조회 응답 DTO
     * @throws MembersCustomException {@link MembersExceptionCode#NOT_FOUND_MEMBER}
     * 멤버를 찾을 수 없는 경우
     */
    MembersResponseDto.ReadOneForVacationListDto readOneForVacationList(
            Long memberId, Members loginMember);

    /**
     * 관리자가 멤버의 잔여 휴가 일수를 수정
     * @param memberId : 수정할 멤버 아이디
     * @param loginMember : 로그인 멤버(HR Manager || CEO)
     * @param requestDto : 멤버의 휴가를 수정하는 요청 DTO
     * @return 멤버의 휴가를 수정하는 응답 DTO
     * (**) 특이 사항 : 자기 자신이 휴가를 수정하고자 하는 경우 Vacation 도메인에 존재.
     * @throws MembersCustomException {@link MembersExceptionCode#VACATION_EXCEEDS_LIMIT}
     * 휴가 요청이 잔여 휴가 일 수보다 15일을 초과한 경우
     * @throws MembersCustomException {@link MembersExceptionCode#VACATION_UNDER_ZERO}
     * 요청한 휴가 일수가 0일 미만일 경우
     */
    MembersResponseDto.UpdateOneForVacationDto updateMemberVacationByAdmin(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForVacationDto requestDto);

    /**
     * 멤버 이름 리스트 조회
     * @param loginMember : 로그인 멤버
     * @return 멤버 이름 리스트 응답 DTO
     */
    List<MembersResponseDto.ReadNameDto> readNameList(Members loginMember);
}
