package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.Members;

public interface MembersService {

    public MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto);

    public void login(MembersRequestDto.LoginMemberRequestDto requestDto);

    public MembersResponseDto.ReadMemberResponseDto readMemberInfo(
            Long memberId, Members member);

    public MembersResponseDto.UpdateMemberResponseDto updateMember(
            Long memberId,  Members Member, MembersRequestDto.UpdateMemberRequestDto requestDto);

    public MembersResponseDto.UpdateMemberRoleResponseDto updateMemberRole(
            Long memberId, Members member, MembersRequestDto.UpdateMemberRoleRequestDto requestDto);

    public void deleteMember(Long memberId, Members member);
}
