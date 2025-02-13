package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MembersResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.CreateOneDto",
            description = "회원가입 응답 DTO")
    public static class CreateOneDto{
        private Long memberId;
        // 접속 아이디
        private String memberName;
        // 실제 이름
        private String name;
        private String email;
        private String contact;
        private String address;
        private MemberRole role;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.ReadAvailableMemberNameDto",
            description = "회원 아이디 중복 체크 여부 조회 응답 DTO")
    public static class ReadAvailableMemberNameDto {
        private Boolean isAvailable;
        private String memberName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.ReadOneDto",
            description = "요약된 멤버 한 명의 정보 조회 응답 DTO")
    public static class ReadOneSummaryDto {
        private Long memberId;
        private String memberName;
        private String email;
        private String address;
        private Long salary;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private MemberDepartment department;
        private MemberPosition position;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.ReadOneDto",
            description = "회원 한 명의 상세보기 조회 응답 DTO")
    public static class ReadOneDetailsDto {
        private Long memberId;
        private String memberName;
        private String name;
        private String email;
        private String address;
        private Long loveCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Boolean onVacation;
        private Integer remainingVacationDays;
        private Long salary;
        private String profileImageUrl;
        private String introduction;
        private MemberDepartment department;
        private MemberPosition position;
        private MemberRole role;
        private String contact;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.UpdateOneDto",
            description = "회원 한 명의 개인 정보 수정 응답 DTO")
    public static class UpdateOneDto{
        private Long memberId;
        private String name;
        private String memberName;
        private String email;
        private String contact;
        private String address;
        private String introduction;
        private String profileImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.UpdateOneForAttributeDto",
            description = "회원 한 명의 중요 정보 수정 응답 DTO")
    public static class UpdateOneForAttributeDto{
        private Long memberId;
        private String memberName;
        private String fileName;
        private Long salary;
        private MemberRole memberRole;
        private MemberPosition memberPosition;
        private MemberDepartment memberDepartment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.UpdateOneForSalaryDto",
            description = "회원 한 명의 급여 수정 응답 DTO")
    public static class UpdateOneForSalaryDto {
        private Long memberId;
        private String memberName;
        private MemberRole memberRole;
        private MemberPosition memberPosition;
        private MemberDepartment memberDepartment;
        private Long changedSalary;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.UpdateOneForProfileImageDto",
            description = "회원 한 명의 프로필 수정 응답 DTO")
    public static class UpdateOneForProfileImageDto{
        private Long memberId;
        private String fromMemberName;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MembersResponseDto.DeleteOneForProfileImageDto",
            description = "회원 한 명의 프로필 삭제 응답 DTO")
    public static class DeleteOneForProfileImageDto{
        private Long memberId;
        private String fromMemberName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "MembersResponseDto.ReadOneForVacationListDto",
            description = "회원 한 명의 휴가 리스트 조회 응답 DTO")
    public static class ReadOneForVacationListDto {
        private MemberPosition position;
        private Integer remainingVacationDays;
        private List<VacationsResponseDto.ReadDayDto> vacationList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "MembersResponseDto.UpdateOneForVacationDto",
            description = "회원 한 명의 휴가 리스트 수정 응답 DTO")
    public static class UpdateOneForVacationDto {
        private Long toMemberId;
        private String toMemberName;
        private Integer changeMemberVacationDays;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "MembersResponseDto.ReadNameDto",
            description = "멤버 이름 리스트 조회 응답 DTO")
    public static class ReadNameDto{
        private Long memberId;
        private String memberName;
        private MemberDepartment department;
        private MemberPosition position;
    }
}
