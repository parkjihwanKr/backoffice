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

@Slf4j(topic = "front -> server jwt token checking")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final JwtProvider jwtProvider;

    @GetMapping("/check-auth")
    public ResponseEntity<CommonResponseDto<AuthDto>> checkAuth(HttpServletRequest request) {
        String token = jwtProvider.getJwtFromHeader(request);
        log.info("checking auth...");
        JwtStatus status = jwtProvider.validateToken(token);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MemberDetailsImpl memberDetails = (MemberDetailsImpl) auth.getPrincipal();
        Members loginMember = memberDetails.getMembers();

        return switch (status) {
            case ACCESS -> ResponseEntity.status(HttpStatus.OK).body(
                    new CommonResponseDto<>(
                            AuthDto.of(
                                    loginMember.getMemberName(),
                                    loginMember.getDepartment().getDepartment(),
                                    loginMember.getPosition().getPosition()
                            ), "인증 절차에 성공하였습니다.", 200
                    )
            );
            case FAIL, EXPIRED -> throw new JwtCustomException(GlobalExceptionCode.NOT_MATCHED_AUTHENTICATION);
            // default -> throw new JwtCustomException(GlobalExceptionCode.INVALID_TOKEN_VALUE);
        };
    }
}
