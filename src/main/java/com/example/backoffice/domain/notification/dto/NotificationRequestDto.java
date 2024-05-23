package com.example.backoffice.domain.notification.dto;

import com.example.backoffice.domain.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class NotificationRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateNotificationRequestDto{
        private String message;
        private List<Long> excludedMemberIdList;
        private List<MemberRole> excludedMemberRole;
    }
}
