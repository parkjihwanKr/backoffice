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

        public Members toEntity(String bcryptPassword) {
            return Members.builder()
                    .memberName(this.memberName)
                    .name(this.name) // 이름을 name으로 설정하는 것이 맞는지 확인
                    .role(this.role) // 역할 설정, MemberRole.USER 또는 직접 설정
                    .email(this.email)
                    .address(this.address)
                    .password(bcryptPassword) // 암호화된 비밀번호 사용
                    .contact(this.contact)
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
