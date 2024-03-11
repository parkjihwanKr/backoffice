package com.example.backoffice.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MembersResponseDto {

    public class MembersCreateResponseDto{
        // 접속 아이디
        private String membername;
        // 실제 이름
        private String name;
        private String password;
        private String passwordConfirm;
        private String email;
    }

    public class MemberReadInfoResponseDto {

        private String username;
        private String email;
        private String address;
    }

    public class MemberUpdateResponseDto{

        private String username;
        private String password;
        private String passwordConfirm;
    }
}
