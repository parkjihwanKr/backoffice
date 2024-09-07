package com.example.backoffice.global.jwt.controller;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.jwt.JwtStatus;
import com.example.backoffice.global.jwt.dto.AuthDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "front -> server jwt token checking")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final JwtProvider jwtProvider;

    @GetMapping("/check-auth")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkAuth(HttpServletRequest request) {
        String token = jwtProvider.getJwtFromHeader(request);
        // 해당 과정에서 이미 로그인 유저의 정보를 SecurityContextHolder에 가지고 있음.

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("isAuthenticated : " + auth.isAuthenticated());

        MemberDetailsImpl memberDetails = (MemberDetailsImpl) auth.getPrincipal();
        Members loginMember = memberDetails.getMember();

        AuthDto authResponseDto = AuthDto.of(
                loginMember.getMemberName(),
                loginMember.getDepartment().getDepartment(),
                loginMember.getPosition().getPosition());

        JwtStatus status = jwtProvider.validateToken(token);
        return switch (status) {
            case ACCESS -> ResponseEntity.status(HttpStatus.OK).body(
                    new CommonResponseDto<>(
                            authResponseDto, "인증 절차에 성공하였습니다.", 200
                    )
            );
            case FAIL, EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.NOT_MATCHED_AUTHENTICATION);
            // default -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        };
    }
}
