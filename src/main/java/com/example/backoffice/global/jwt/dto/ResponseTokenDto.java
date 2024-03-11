package com.example.backoffice.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTokenDto {
    // 해당 토큰들은 cookie에 사용될 예정
    private String accessToken;
    private String refreshToken;
}
