package com.example.backoffice.global.jwt.controller;

import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.jwt.dto.AuthDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @GetMapping("/check-auth")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkAuth(Authentication authentication) {
        MemberDetailsImpl memberDetails = (MemberDetailsImpl) authentication.getPrincipal();
        AuthDto authResponseDto = AuthDto.of(
                memberDetails.getMembers().getId(), memberDetails.getMembers().getMemberName(),
                memberDetails.getMembers().getDepartment().getDepartment(),
                memberDetails.getMembers().getPosition().getPosition(),
                memberDetails.getMembers().getProfileImageUrl()
        );
        return ResponseEntity.ok(new CommonResponseDto<>(authResponseDto, "인증 절차에 성공하였습니다.", 200));
    }

    @GetMapping("/access-token")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkAccessToken(){
        return null;
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkRefreshToken(Authentication authentication) {
        MemberDetailsImpl memberDetails = (MemberDetailsImpl) authentication.getPrincipal();
        AuthDto authResponseDto = AuthDto.of(
                memberDetails.getMembers().getId(), memberDetails.getMembers().getMemberName(),
                memberDetails.getMembers().getDepartment().getDepartment(),
                memberDetails.getMembers().getPosition().getPosition(),
                memberDetails.getMembers().getProfileImageUrl()
        );
        return ResponseEntity.ok(new CommonResponseDto<>(authResponseDto, "인증 절차에 성공하였습니다.", 200));
    }
}
