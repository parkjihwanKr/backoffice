package com.example.backoffice.domain.asset.controller;

import com.example.backoffice.domain.asset.dto.AssetResponseDto;
import com.example.backoffice.domain.asset.service.AssetServiceV1;
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
public class AssetController {

    private final AssetServiceV1 assetService;

    @GetMapping("/asset")
    public ResponseEntity<AssetResponseDto.ReadOneDto> readOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AssetResponseDto.ReadOneDto responseDto
                = assetService.readOne(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
