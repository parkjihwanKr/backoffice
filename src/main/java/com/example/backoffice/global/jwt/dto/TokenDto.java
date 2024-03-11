package com.example.backoffice.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    // accessToken 만료 시
    public TokenDto createAccessToken(String accessToken) {
        return new TokenDto(accessToken, refreshToken);
    }

    // refreshToken 만료 시
    public TokenDto createToken(String accessToken, String refreshToken){
        return new TokenDto(accessToken, refreshToken);
    }
}
