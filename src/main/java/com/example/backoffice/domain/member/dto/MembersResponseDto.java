package com.example.backoffice.domain.member.dto;

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
    public static class CreateMembersResponseDto{
        // 접속 아이디
        private String memberName;
        // 실제 이름
        private String name;
        private String email;
        private String contact;
        private String address;
        private MemberRole role;

        public static CreateMembersResponseDto from(Members member){
            return CreateMembersResponseDto.builder()
                    .email(member.getEmail())
                    .memberName(member.getMemberName())
                    .name(member.getMemberName())
                    .contact(member.getContact())
                    .role(member.getRole())
                    .address(member.getAddress())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadMemberResponseDto {

        private String memberName;
        private String email;
        private String address;
        private MemberRole role;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public static ReadMemberResponseDto from(Members member){
            return ReadMemberResponseDto.builder()
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .memberName(member.getMemberName())
                    .role(member.getRole())
                    .createdAt(member.getCreatedAt())
                    .modifiedAt(member.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberResponseDto{
        private String name;
        private String memberName;
        private String email;
        private String contact;
        private String address;
        private String introduction;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public static UpdateMemberResponseDto from(Members member){
            return UpdateMemberResponseDto.builder()
                    .memberName(member.getMemberName())
                    .name(member.getName())
                    .address(member.getAddress())
                    .contact(member.getContact())
                    .email(member.getEmail())
                    .introduction(member.getIntroduction())
                    .createdAt(member.getCreatedAt())
                    .modifiedAt(member.getModifiedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberRoleResponseDto{
        private MemberRole role;

        public static UpdateMemberRoleResponseDto from(Members member){
            return UpdateMemberRoleResponseDto.builder()
                    .role(member.getRole())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberProfileImageUrlResponseDto{
        private String profileImageUrl;

        public static UpdateMemberProfileImageUrlResponseDto from(Members member){
            return UpdateMemberProfileImageUrlResponseDto.builder()
                    .profileImageUrl(member.getProfileImageUrl())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMemberProfileImageUrlResponseDto{
        private String profileImageUrl;

        public static DeleteMemberProfileImageUrlResponseDto of(Members member){
            return DeleteMemberProfileImageUrlResponseDto.builder()
                    .profileImageUrl(member.getProfileImageUrl())
                    .build();
        }
    }
}
