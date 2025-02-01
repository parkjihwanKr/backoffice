package com.example.backoffice.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "MembersRequestDto.CreateOneDto", description = "회원가입 요청 DTO")
    public static class CreateOneDto{

        @Schema(description = "비밀번호", example = "10002000")
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        private String password;

        @Schema(description = "비밀번호 확인", example = "10002000")
        @Size(min = 8, max = 16, message = "비밀번호 확인은 8자 이상 16자 이하로 입력해주세요.")
        private String passwordConfirm;

        @Schema(description = "이메일", example = "test@naver.com")
        private String email;

        @Schema(description = "실명", example = "아무개")
        private String name;

        @Schema(description = "회원 접속 아이디", example = "player1234")
        private String memberName;

        @Schema(description = "상세주소", example = "경기도 용인시 처인구 모현읍")
        private String address;

        @Schema(description = "연락처", example = "010-2235-1232")
        private String contact;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "MemberRequestDto.LoginDto", description = "로그인 요청 DTO")
    public static class LoginDto{
        private String memberName;
        private String password;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "MembersRequestDto.UpdateOneDto", description = "개인 정보 수정 요청 DTO")
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
    @Schema(name = "MembersRequestDto.UpdateOneForAttributeDto",
            description = "관리자의 특정 멤버 인사 수정 요청 DTO")
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
    @Schema(name = "MembersRequestDto.UpdateOneForSalaryDto",
            description = "관리자의 특정 멤버 연봉 수정 요청 DTO")
    public static class UpdateOneForSalaryDto{
        private String memberName;
        private Long salary;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "MembersRequestDto.UpdateOneForSalaryDto",
            description = "관리자의 특정 멤버 휴가 일 수 수정 요청 DTO")
    public static class UpdateOneForVacationDto{
        private Integer vacationDays;
    }
}
