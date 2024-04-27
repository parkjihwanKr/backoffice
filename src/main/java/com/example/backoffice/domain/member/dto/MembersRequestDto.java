package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.weaver.Member;

public class MembersRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMembersRequestDto{

        //@Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "username은 최소 3자 이상이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
        private String username;

        //@Pattern(regexp = "^[a-zA-Z0-9]{4,}$", message = "password는 최소 4자 이상이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.")
        private String password;

        //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "유효한 이메일 주소를 입력하세요.")
        private String email;

        private String name;
        private String memberName;
        // private String email;
        private String address;
        // private String password;
        private String passwordConfirm;
        private String contact;

        public Members toEntity(String bcryptPassword) {
            return Members.builder()
                    .memberName(this.memberName)
                    .name(this.name) // 이름을 name으로 설정하는 것이 맞는지 확인
                    .role(MemberRole.USER) // 역할 설정, MemberRole.USER 또는 직접 설정
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
        private String passwordConfirm;
        private String email;
        private String address;
        private String contact;
        private String introduction;
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
