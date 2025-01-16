package com.example.backoffice.domain.member.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MembersRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        private String password;

        @Size(min = 8, max = 16, message = "비밀번호 확인은 8자 이상 16자 이하로 입력해주세요.")
        private String passwordConfirm;

        private String email;
        private String name;
        private String memberName;
        private String address;
        private String contact;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDto{
        private String memberName;
        private String password;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneDto{
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
    public static class UpdateOneForAttributeDto {
        private String memberName;
        private String role;
        private String department;
        private String position;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForSalaryDto{
        private String memberName;
        private Long salary;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForProfileImageDto {
        private String password;
        private String passwordConfirm;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForVacationDto{
        private Integer vacationDays;
    }
}
