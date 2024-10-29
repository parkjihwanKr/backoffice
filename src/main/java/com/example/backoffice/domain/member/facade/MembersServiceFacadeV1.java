package com.example.backoffice.domain.member.facade;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.global.scheduler.ScheduledEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MembersServiceFacadeV1 {
    MembersResponseDto.CreateOneDto createOneForSignup(
            MembersRequestDto.CreateOneDto requestDto);

    MembersResponseDto.ReadOneDetailsDto readOne(
            Long memberId, Members member);


    Page<MembersResponseDto.ReadOneDto> readForHrManager(
            String department, String position,
            Members loginMember, Pageable pageable);

    MembersResponseDto.UpdateOneDto updateOne(
            Long memberId,  Members Member, MultipartFile multipartFile,
            MembersRequestDto.UpdateOneDto requestDto);

    MembersResponseDto.UpdateOneForAttributeDto updateOneForAttribute(
            Long memberId, Members member,
            MembersRequestDto.UpdateOneForAttributeDto requestDto,
            MultipartFile multipartFile) throws MembersCustomException;

    MembersResponseDto.UpdateOneForProfileImageDto updateOneForProfileImage(
            Long memberId, Members member, MultipartFile image);

    MembersResponseDto.DeleteOneForProfileImageDto deleteOneForProfileImage(
            Long memberId, Members member);

    void deleteOne(Long memberId, Members member);

    Members matchLoginMember(Members member, Long memberId);

    Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    MembersResponseDto.UpdateOneForSalaryDto updateOneForSalary(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForSalaryDto requestDto);

    MembersResponseDto.ReadOneForVacationListDto readOneForVacationList(
            Long memberId, Members loginMember);

    MembersResponseDto.UpdateOneForVacationDto updateOneForVacation(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForVacationDto requestDto);

    List<MembersResponseDto.ReadNameDto> readNameList(Members loginMember);

    void updateOneForOnVacationFalse(Long onVacationMemberId);

    void updateOneForOnVacationTrue(Long onVacationMemberId);

    void updateOneForRemainingVacationDays(ScheduledEventType scheduledEventType);

    List<Members> findAllByDepartment(String department);

    Members findHRManager();

    Members findByMemberName(String memberName);
}
