package com.example.backoffice.domain.member.converter;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;

public class MembersConverter {

    public static Members toAdminEntity(String bcrytPassword){
        return Members.builder()
                .memberName("admin")
                .name("admin")
                .loveCount(0L)
                .role(MemberRole.ADMIN)
                .email("admin@test.com")
                .address("admin시 admin동")
                .introduction("admin이다")
                .memberDepartment(MemberDepartment.HR)
                .password(bcrytPassword)
                .contact("010-0000-0000")
                .position(MemberPosition.CEO)
                .salary(20000000L)
                .build();
    }
    public static Members toEntity(
            MembersRequestDto.CreateMembersRequestDto requestDto, String bcryptPassword) {
        return Members.builder()
                .memberName(requestDto.getMemberName())
                .name(requestDto.getName()) // 이름을 name으로 설정하는 것이 맞는지 확인
                .role(MemberRole.USER) // 역할 설정, MemberRole.USER 또는 직접 설정
                .memberDepartment(MemberDepartment.HR)
                .position(MemberPosition.INTERN)
                .email(requestDto.getEmail())
                .address(requestDto.getAddress())
                .loveCount(0L)
                .password(bcryptPassword) // 암호화된 비밀번호 사용
                .contact(requestDto.getContact())
                .build();
    }

    public static MembersResponseDto.CreateMembersResponseDto toCreateDto(Members member){
        return MembersResponseDto.CreateMembersResponseDto.builder()
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .name(member.getMemberName())
                .contact(member.getContact())
                .role(member.getRole())
                .address(member.getAddress())
                .build();
    }

    public static MembersResponseDto.ReadMemberResponseDto toReadDto(Members member){
        return MembersResponseDto.ReadMemberResponseDto.builder()
                .email(member.getEmail())
                .address(member.getAddress())
                .memberName(member.getMemberName())
                .role(member.getRole())
                .loveCount(member.getLoveCount())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }

    public static MembersResponseDto.UpdateMemberResponseDto toUpdateDto(Members member){
        return MembersResponseDto.UpdateMemberResponseDto.builder()
                .memberName(member.getMemberName())
                .name(member.getName())
                .address(member.getAddress())
                .contact(member.getContact())
                .email(member.getEmail())
                .introduction(member.getIntroduction())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }

    public static MembersResponseDto.UpdateMemberAttributeResponseDto toUpdateAttributeDto(Members member, String document){
        return MembersResponseDto.UpdateMemberAttributeResponseDto.builder()
                .fromMemberName(member.getMemberName())
                .fileName(document)
                .build();
    }

    public static MembersResponseDto.UpdateMemberProfileImageUrlResponseDto toUpdateProfileImageDto(Members member){
        return MembersResponseDto.UpdateMemberProfileImageUrlResponseDto.builder()
                .fromMemberName(member.getMemberName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    public static MembersResponseDto.DeleteMemberProfileImageResponseDto toDeleteProfileImageDto(Members member){
        return MembersResponseDto.DeleteMemberProfileImageResponseDto.builder()
                .fromMemberName(member.getMemberName())
                .build();
    }
}
