package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MembersRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMembersRequestDto{
        private String name;
        private String memberName;
        private MemberRole role;
        private String email;
        private String address;
        private String password;
        private String passwordConfirm;
        private String contact;

        public static Members from(CreateMembersRequestDto requestDto, String bCrytPassword){
            return Members.builder()
                    .memberName(requestDto.getMemberName())
                    .role(MemberRole.USER)
                    .email(requestDto.getEmail())
                    .address(requestDto.getAddress())
                    .password(bCrytPassword)
                    .contact(requestDto.getContact())
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginMemberRequestDto{
        private String memberName;
        private String password;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberRequestDto{
        private String name;
        private String memberName;
        private String password;
        private String passwordCofirm;
        private String email;
        private String address;
        private String contact;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberRoleRequestDto {
        private String password;
        private String passwordConfirm;
        private MemberRole role;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberProfileImageUrlRequestDto {
        private String password;
        private String passwordConfirm;
        private String profileImageUrl;
    }
}
