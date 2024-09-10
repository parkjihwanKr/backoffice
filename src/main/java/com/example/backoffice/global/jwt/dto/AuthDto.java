package com.example.backoffice.global.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private String department;
    private String position;
    private String memberName;

    public static AuthDto of(String memberName, String department, String position) {
        return new AuthDto(memberName, department, position);
    }
}
