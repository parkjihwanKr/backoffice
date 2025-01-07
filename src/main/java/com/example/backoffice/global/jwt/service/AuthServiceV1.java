package com.example.backoffice.global.jwt.service;

import com.example.backoffice.global.jwt.dto.AuthDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthServiceV1 {

    /**
     * 로그인한 멤버가 유효한 상태의 AccessToken을 가지고 접근하는지 확인
     * @param request : HttpServletRequest / AccessToken을 확인하기 위해
     * @return : {@link AuthDto}
     * 해당 멤버의 기초적인 정보 : 아이디, 이름, 부서, 직위, 프로필 사진
     */
    AuthDto checkAuth(HttpServletRequest request);
}
