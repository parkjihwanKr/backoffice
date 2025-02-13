package com.example.backoffice.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthDto {

    private Long id;
    private String name;
    private String department;
    private String position;
    private String profileImageUrl;

    public static AuthDto of(
            Long id, String name, String department,
            String position, String profileImageUrl){
        return new AuthDto(id, name, department, position, profileImageUrl);
    }
}
