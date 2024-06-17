package com.example.backoffice.domain.favorite.controller;

import com.example.backoffice.domain.favorite.dto.FavoritiesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritiesResponseDto;
import com.example.backoffice.domain.favorite.service.FavoritiesService;
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

    private final FavoritiesService favoritiesService;

    // 즐겨찾기 생성
    @PostMapping("/favorities")
    public ResponseEntity<FavoritiesResponseDto.CreateFavoriteResponseDto> createFavorite(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody FavoritiesRequestDto.CreateFavoriteRequestDto requestDto) {
        FavoritiesResponseDto.CreateFavoriteResponseDto responseDto
                = favoritiesService.createFavorite(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 1개 조회
    @GetMapping("/favorities/{favoriteId}")
    public ResponseEntity<FavoritiesResponseDto.ReadFavoriteResponseDto> readFavorite(
            @PathVariable Long favoriteId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        FavoritiesResponseDto.ReadFavoriteResponseDto responseDto
                = favoritiesService.readFavorite(favoriteId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 모두 조회
    @GetMapping("/favorities")
    public ResponseEntity<List<FavoritiesResponseDto.ReadFavoriteResponseDto>> readFavoriteList(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        List<FavoritiesResponseDto.ReadFavoriteResponseDto> responseDtoList
                = favoritiesService.readFavoriteList(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/favorities")
    public ResponseEntity<CommonResponseDto<Void>> deleteFavorite(
            @RequestBody FavoritiesRequestDto.DeleteFavoriteIdListRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        favoritiesService.deleteFavorite(requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "즐겨찾기 삭제 성공", 200
                )
        );
    }
}
