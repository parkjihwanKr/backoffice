package com.example.backoffice.global.jwt.controller;

import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.jwt.CookieUtil;
import com.example.backoffice.global.jwt.dto.AuthDto;
import com.example.backoffice.global.jwt.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check-auth")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkAuth(Authentication authentication) {
        AuthDto authResponseDto
                = authService.checkAuth(authentication);
        return ResponseEntity.ok(new CommonResponseDto<>(authResponseDto, "인증 절차에 성공하였습니다.", 200));
    }

    @GetMapping("/access-token")
    public ResponseEntity<CommonResponseDto<String>> getAccessToken(
            @CookieValue(name = "accessToken", required = false) String accessTokenValue,
            @CookieValue(name = "refreshToken", required = false) String refreshTokenValue,
            HttpServletResponse response){
        List<String> tokenList
                = authService.getToken(accessTokenValue, refreshTokenValue);
        if (tokenList.size() == 4){
            if(tokenList.get(2) == null){
                response.setHeader(CookieUtil.SET_COOKIE, tokenList.get(3));
            }else if(tokenList.get(3) == null){
                response.setHeader(CookieUtil.SET_COOKIE, tokenList.get(2));
            }else if(tokenList.get(2) != null && tokenList.get(3) != null){
                response.setHeader(CookieUtil.SET_COOKIE, "");
                response.addHeader(CookieUtil.SET_COOKIE, tokenList.get(2));
                response.addHeader(CookieUtil.SET_COOKIE, tokenList.get(3));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        tokenList.get(0),
                        "정상적으로 액세스 토큰을 가져왔습니다.",
                        200));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkRefreshToken(Authentication authentication) {
        AuthDto authResponseDto
                = authService.checkAuth(authentication);
        return ResponseEntity.ok(new CommonResponseDto<>(authResponseDto, "인증 절차에 성공하였습니다.", 200));
    }
}
