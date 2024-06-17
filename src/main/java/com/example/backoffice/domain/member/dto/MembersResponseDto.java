package com.example.backoffice.domain.member.dto;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
    public static class ReadOneProfileDto {
        private String memberName;
        private String email;
        private String address;
        private MemberRole role;
        private Long loveCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Boolean onVacation;
        private Integer remainingVacationDays;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneProfileDto{
        private String name;
        private String memberName;
        private String email;
        private String contact;
        private String address;
        private String introduction;
        private Long loveCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneAttributeDto{
        private String memberName;
        private String fileName;
        private MemberRole memberRole;
        private MemberPosition memberPosition;
        private MemberDepartment memberDepartment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneSalaryDto {
        private String memberName;
        private MemberRole memberRole;
        private MemberPosition memberPosition;
        private MemberDepartment memberDepartment;
        private Long changedSalary;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateOneVacationDaysDto {
        // 잔여 휴가 일 수
        private Integer vacationDays;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOneProfileImageDto{
        private String fromMemberName;
        private String profileImageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteOneProfileImageDto{
        private String fromMemberName;
    }
}
