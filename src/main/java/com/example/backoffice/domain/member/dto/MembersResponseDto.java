package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class MembersResponseDto {

    @Builder
    public static class CreateMembersResponseDto{
        // 접속 아이디
        private String memberName;
        // 실제 이름
        private String name;
        private String email;
        private String contact;
        private String address;

        public static CreateMembersResponseDto from(Members member){
            return CreateMembersResponseDto.builder()
                    .email(member.getEmail())
                    .memberName(member.getMemberName())
                    .name(member.getMemberName())
                    .contact(member.getContact())
                    .address(member.getAddress())
                    .build();
        }
    }

    @Builder
    public static class ReadMemberResponseDto {

        private String memberName;
        private String email;
        private String address;

        public static ReadMemberResponseDto from(Members member){
            return ReadMemberResponseDto.builder()
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .memberName(member.getMemberName())
                    .build();
        }
    }

    @Builder
    public static class UpdateMemberResponseDto{

        private String memberName;
        private String username;
        private String email;
        private String contact;
        private String address;

        public static UpdateMemberResponseDto from(Members member){
            return UpdateMemberResponseDto.builder()
                    .memberName(member.getMemberName())
                    .username(member.getName())
                    .address(member.getAddress())
                    .contact(member.getContact())
                    .email(member.getEmail())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberRoleResponseDto{
        private MemberRole role;

        public static UpdateMemberRoleResponseDto from(Members member){
            return UpdateMemberRoleResponseDto.builder()
                    .role(member.getRole())
                    .build();
        }
    }

    @Builder
    public static class UpdateMemberProfileImageUrlResponseDto{
        private String profileImageUrl;

        public static UpdateMemberProfileImageUrlResponseDto from(Members member){
            return UpdateMemberProfileImageUrlResponseDto.builder()
                    .profileImageUrl(member.getProfileImageUrl())
                    .build();
        }
    }

    @Builder
    public static class DeleteMemberProfileImageUrlResponseDto{
        private String profileImageUrl;

        public static DeleteMemberProfileImageUrlResponseDto of(Members member){
            return DeleteMemberProfileImageUrlResponseDto.builder()
                    .profileImageUrl(member.getProfileImageUrl())
                    .build();
        }
    }
}
