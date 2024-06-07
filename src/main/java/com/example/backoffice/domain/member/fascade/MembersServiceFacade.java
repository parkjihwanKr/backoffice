package com.example.backoffice.domain.member.fascade;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MembersServiceFacade {
    MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto);

    MembersResponseDto.ReadMemberResponseDto readInfo(
            Long memberId, Members member);

    MembersResponseDto.UpdateMemberResponseDto updateMember(
            Long memberId,  Members Member,
            MembersRequestDto.UpdateMemberRequestDto requestDto);

    MembersResponseDto.UpdateMemberAttributeResponseDto updateAttribute(
            Long memberId, Members member,
            MembersRequestDto.UpdateMemberAttributeRequestDto requestDto);

    MembersResponseDto.UpdateMemberProfileImageUrlResponseDto updateProfileImageUrl(
            Long memberId, Members member, MultipartFile image);

    MembersResponseDto.DeleteMemberProfileImageResponseDto deleteProfileImage(
            Long memberId, Members member);

    void deleteMember(Long memberId, Members member);

    Members findMember(Members member, Long memberId);

    Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    Members findAdmin(
            Long adminId, MemberRole role, MemberDepartment department);

    MembersResponseDto.UpdateMemberSalaryResponseDto updateSalary(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateMemberSalaryRequestDto requestDto);

    void updateOnVacationFalse(String memberName);

    void updateOnVacationTrue(String memberName);
}
