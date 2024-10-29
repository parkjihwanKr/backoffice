package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
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
    public static class ReadOneDto {
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
    public static class ReadOneDetailsDto {
        private Long memberId;
        private String memberName;
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
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    public static class UpdateOneForProfileImageDto{
        private Long memberId;
        private String fromMemberName;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteOneForProfileImageDto{
        private Long memberId;
        private String fromMemberName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForVacationDaysDto {
        private Long memberId;
        // 잔여 휴가 일 수
        private Integer vacationDays;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadOneForVacationListDto {
        private MemberPosition position;
        private Integer remainingVacationDays;
        private List<VacationsResponseDto.ReadDayDto> vacationList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForVacationDto {
        private Long toMemberId;
        private String toMemberName;
        private Integer changeMemberVacationDays;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReadNameDto{
        private Long memberId;
        private String memberName;
    }
}
