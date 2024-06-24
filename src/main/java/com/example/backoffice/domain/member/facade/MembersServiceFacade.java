package com.example.backoffice.domain.member.facade;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.scheduler.ScheduledEventType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MembersServiceFacade {
    MembersResponseDto.CreateOneDto signup(
            MembersRequestDto.CreateOneDto requestDto);

    MembersResponseDto.ReadOneDto readOne(
            Long memberId, Members member);

    MembersResponseDto.UpdateOneDto updateOne(
            Long memberId,  Members Member, MultipartFile multipartFile,
            MembersRequestDto.UpdateOneDto requestDto);

    MembersResponseDto.UpdateOneForAttributeDto updateOneForAttribute(
            Long memberId, Members member,
            MembersRequestDto.UpdateOneForAttributeDto requestDto,
            MultipartFile multipartFile);

    MembersResponseDto.UpdateOneForProfileImageDto updateOneForProfileImage(
            Long memberId, Members member, MultipartFile image);

    MembersResponseDto.DeleteOneForProfileImageDto deleteOneForProfileImage(
            Long memberId, Members member);

    void deleteOne(Long memberId, Members member);

    Members findMember(Members member, Long memberId);

    Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    Members findAdmin(
            Long adminId, MemberRole role, MemberDepartment department);

    MembersResponseDto.UpdateOneForSalaryDto updateOneForSalary(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForSalaryDto requestDto);

    void updateOneForOnVacationFalse(String memberName);

    void updateOneForOnVacationTrue(String memberName);

    void updateOneForRemainingVacationDays(ScheduledEventType scheduledEventType);

    List<Members> findAllByDepartment(String department);

    List<Members> findAllByPosition(String position);
}
