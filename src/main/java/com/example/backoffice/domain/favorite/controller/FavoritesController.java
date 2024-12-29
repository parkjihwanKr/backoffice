package com.example.backoffice.domain.favorite.controller;

import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.service.FavoritesServiceV1;
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
public class FavoritesController {

    private final FavoritesServiceV1 favoritesService;

    // 즐겨찾기 생성
    @PostMapping("/favorites")
    public ResponseEntity<FavoritesResponseDto.CreateOneDto> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody FavoritesRequestDto.CreateOneDto requestDto){
        FavoritesResponseDto.CreateOneDto responseDto
                = favoritesService.createOne(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 1개 조회
    @GetMapping("/favorites/{favoriteId}")
    public ResponseEntity<FavoritesResponseDto.ReadOneDto> readOne(
            @PathVariable Long favoriteId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        FavoritesResponseDto.ReadOneDto responseDto
                = favoritesService.readOne(favoriteId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 모두 조회
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoritesResponseDto.ReadOneDto>> readAll(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<FavoritesResponseDto.ReadOneDto> responseDtoList
                = favoritesService.readAll(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PatchMapping("/favorites/{favoritesId}")
    public ResponseEntity<FavoritesResponseDto.UpdateOneDto> updateOne(
            @PathVariable Long favoritesId,
            @RequestBody FavoritesRequestDto.UpdateOneDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        FavoritesResponseDto.UpdateOneDto responseDto
                = favoritesService.updateOne(
                        favoritesId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 삭제
    @DeleteMapping("/favorites/{favoritesId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOne(
            @PathVariable Long favoritesId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        favoritesService.deleteOne(favoritesId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "즐겨찾기 삭제 성공", 200
                )
        );
    }
}
