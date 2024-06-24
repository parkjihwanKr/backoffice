package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MembersResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOneDto{
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

        private String memberName;
        private String email;
        private String address;
        private MemberRole role;
        private Long loveCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Boolean onVacation;
        private Integer remainingVacationDays;
        private MemberDepartment department;
        private MemberPosition position;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneDto{
        private String name;
        private String memberName;
        private String email;
        private String contact;
        private String address;
        private String introduction;
        private Long loveCount;
        private String profileImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneForAttributeDto{
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
        private String fromMemberName;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteOneForProfileImageDto{
        private String fromMemberName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneForVacationDaysDto {
        // 잔여 휴가 일 수
        private Integer vacationDays;
    }
}
