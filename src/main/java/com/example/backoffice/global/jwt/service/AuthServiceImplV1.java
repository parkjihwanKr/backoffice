package com.example.backoffice.global.jwt.service;

import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.JwtCustomException;
import com.example.backoffice.global.jwt.JwtProvider;
import com.example.backoffice.global.jwt.JwtStatus;
import com.example.backoffice.global.jwt.dto.AuthDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "Auth checking...")
@RequiredArgsConstructor
public class AuthServiceImplV1 implements AuthServiceV1{

    private final JwtProvider jwtProvider;

    @Override
    public AuthDto checkAuth(HttpServletRequest request){
        String accessToken = jwtProvider.getJwtFromHeader(request);
        // 해당 과정에서 이미 로그인 유저의 정보를 SecurityContextHolder에 가지고 있음.

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("isAuthenticated : " + auth.isAuthenticated());

        MemberDetailsImpl memberDetails = (MemberDetailsImpl) auth.getPrincipal();
        Members loginMember = memberDetails.getMembers();

        AuthDto authResponseDto = AuthDto.of(
                loginMember.getId(),
                loginMember.getName(),
                loginMember.getDepartment().getDepartment(),
                loginMember.getPosition().getPosition(),
                loginMember.getProfileImageUrl());

        JwtStatus status = jwtProvider.validateToken(accessToken);
        return switch (status) {
            case ACCESS -> authResponseDto;
            // access Token이 FAIL 상태이면 잘못된 토큰을 가지고 온거기에 에러
            // access Token이 EXPIRED 상태이면 만기된 토큰임으로 다른 API 요청을 통해 전달하기에 에러
            // 해당 방법은 바로 서버에서 API 요청이 가도록 하기에
            // {@link }
            case FAIL, EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.NOT_MATCHED_AUTHENTICATION);
            // default -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        };

    }
}
