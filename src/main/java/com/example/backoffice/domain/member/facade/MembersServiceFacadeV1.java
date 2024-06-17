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

public interface MembersServiceFacadeV1 {
    MembersResponseDto.CreateOneDto signup(
            MembersRequestDto.CreateOneDto requestDto);

    MembersResponseDto.ReadOneProfileDto readOneProfile(
            Long memberId, Members member);

    MembersResponseDto.UpdateOneProfileDto updateOneProfile(
            Long memberId,  Members Member,
            MembersRequestDto.UpdateOneProfileDto requestDto);

    MembersResponseDto.UpdateOneAttributeDto updateOneAttribute(
            Long memberId, Members member,
            MembersRequestDto.UpdateOneAttributeDto requestDto);

    MembersResponseDto.UpdateOneProfileImageDto updateOneProfileImage(
            Long memberId, Members member, MultipartFile image);

    MembersResponseDto.DeleteOneProfileImageDto deleteOneProfileImage(
            Long memberId, Members member);

    MembersResponseDto.UpdateOneSalaryDto updateOneSalary(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneSalaryDto requestDto);

    void deleteOne(Long memberId, Members member);

    Members findOne(Members member, Long memberId);

    Map<String, MemberDepartment> findByMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    Members findAdmin(
            Long adminId, MemberRole role, MemberDepartment department);

    void updateOnVacationFalse(String memberName);

    void updateOnVacationTrue(String memberName);

    void updateRemainingVacationDays(ScheduledEventType scheduledEventType);

    List<Members> findAllByDepartment(String department);

    List<Members> findAllByPosition(String position);
}
