package com.example.backoffice.domain.favorite.controller;

import com.example.backoffice.domain.favorite.dto.FavoritiesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritiesResponseDto;
import com.example.backoffice.domain.favorite.service.FavoritiesServiceV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoritiesController {

    private final FavoritiesServiceV1 favoritiesService;

    // 즐겨찾기 생성
    @PostMapping("/favorities")
    public ResponseEntity<FavoritiesResponseDto.CreateOneDto> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody FavoritiesRequestDto.CreateOneDto requestDto){
        FavoritiesResponseDto.CreateOneDto responseDto
                = favoritiesService.createOne(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 1개 조회
    @GetMapping("/favorities/{favoriteId}")
    public ResponseEntity<FavoritiesResponseDto.ReadOneDto> readOne(
            @PathVariable Long favoriteId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        FavoritiesResponseDto.ReadOneDto responseDto
                = favoritiesService.readOne(favoriteId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 모두 조회
    @GetMapping("/favorities")
    public ResponseEntity<List<FavoritiesResponseDto.ReadOneDto>> readAll(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<FavoritiesResponseDto.ReadOneDto> responseDtoList
                = favoritiesService.readAll(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/favorities")
    public ResponseEntity<CommonResponseDto<Void>> delete(
            @RequestBody FavoritiesRequestDto.DeleteDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        favoritiesService.delete(requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "즐겨찾기 삭제 성공", 200
                )
        );
    }
}
