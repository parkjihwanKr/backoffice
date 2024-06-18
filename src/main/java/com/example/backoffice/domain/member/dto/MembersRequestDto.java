package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.Member;
import org.springframework.web.multipart.MultipartFile;

public class MembersRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMembersRequestDto{
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
    public static class UpdateMemberAttributeRequestDto {
        @NotNull
        private String memberName;
        private Long salary;
        private MemberRole role;
        private MemberDepartment department;
        private MemberPosition position;
        private MultipartFile file;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberSalaryRequestDto{
        private String memberName;
        private Long salary;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberVacationDaysRequestDto {
        // 잔여 휴가 일 수
        private Integer vacationDays;
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
