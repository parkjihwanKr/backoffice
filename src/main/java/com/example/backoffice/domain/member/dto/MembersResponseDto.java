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
        private String fromMemberName;
        private MemberRole role;
        private String fileName;

        public static UpdateMemberRoleResponseDto from(Members member, String document){
            return UpdateMemberRoleResponseDto.builder()
                    .fromMemberName(member.getMemberName())
                    .role(member.getRole())
                    .fileName(document)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMemberProfileImageUrlResponseDto{
        private String fromMemberName;
        private String profileImageUrl;

        public static UpdateMemberProfileImageUrlResponseDto from(Members member){
            return UpdateMemberProfileImageUrlResponseDto.builder()
                    .fromMemberName(member.getMemberName())
                    .profileImageUrl(member.getProfileImageUrl())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteMemberProfileImageResponseDto{
        private String fromMemberName;

        public static DeleteMemberProfileImageResponseDto from(Members member){
            return DeleteMemberProfileImageResponseDto.builder()
                    .fromMemberName(member.getMemberName())
                    .build();
        }
    }
}
