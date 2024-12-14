package com.example.backoffice.domain.mainPage.controller;

import com.example.backoffice.domain.mainPage.dto.MainPageResponseDto;
import com.example.backoffice.domain.mainPage.service.MainPageService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/main-page")
    public ResponseEntity<MainPageResponseDto> read(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        return ResponseEntity.status(HttpStatus.OK).body(
                mainPageService.read(memberDetails.getMembers()));
    }
}
