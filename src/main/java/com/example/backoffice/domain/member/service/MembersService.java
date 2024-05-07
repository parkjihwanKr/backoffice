package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.security.MemberDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

public interface MembersService {

    MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto);

    void login(MembersRequestDto.LoginMemberRequestDto requestDto);

    MembersResponseDto.ReadMemberResponseDto readMemberInfo(
            Long memberId, Members member);

    MembersResponseDto.UpdateMemberResponseDto updateMember(
            Long memberId,  Members Member,
            MembersRequestDto.UpdateMemberRequestDto requestDto);

    MembersResponseDto.UpdateMemberRoleResponseDto updateMemberRole(
            Long memberId, Members member,
            MultipartFile file);

    MembersResponseDto.UpdateMemberProfileImageUrlResponseDto updateMemberProfileImageUrl(
            Long memberId, Members member, MultipartFile image);

    MembersResponseDto.DeleteMemberProfileImageResponseDto deleteMemberProfileImage(
            Long memberId, Members member);

    void deleteMember(Long memberId, Members member);

    Members findMember(Members member, Long memberId);

    Members validateMember(Long toMemberId, Long fromMemberId);
}
