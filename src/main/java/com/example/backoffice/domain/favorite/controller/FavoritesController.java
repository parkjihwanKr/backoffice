package com.example.backoffice.domain.favorite.controller;

import com.example.backoffice.domain.board.dto.BoardsResponseDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.favorite.dto.FavoritesRequestDto;
import com.example.backoffice.domain.favorite.dto.FavoritesResponseDto;
import com.example.backoffice.domain.favorite.service.FavoritesServiceV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Favorites API", description = "즐겨찾기 API")
public class FavoritesController {

    private final FavoritesServiceV1 favoritesService;

    // 즐겨찾기 생성
    @PostMapping("/favorites")
    @Operation(summary = "즐겨 찾기 하나 생성",
            description = "로그인한 사용자가 해당 URL에 대한 즐겨찾기를 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨 찾기 하나 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritesResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<FavoritesResponseDto.CreateOneDto> createOne(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody FavoritesRequestDto.CreateOneDto requestDto){
        FavoritesResponseDto.CreateOneDto responseDto
                = favoritesService.createOne(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 1개 조회
    @GetMapping("/favorites/{favoriteId}")
    @Operation(summary = "즐겨 찾기 하나 조회",
            description = "로그인한 사용자가 자신의 즐겨찾기 하나를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨 찾기 하나 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritesResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<FavoritesResponseDto.ReadOneDto> readOne(
            @PathVariable Long favoriteId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        FavoritesResponseDto.ReadOneDto responseDto
                = favoritesService.readOne(favoriteId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 즐겨찾기 모두 조회
    @GetMapping("/favorites")
    @Operation(summary = "즐겨 찾기 리스트 조회",
            description = "로그인한 사용자가 자신의 즐겨찾기 리스트를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨 찾기 리스트 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = FavoritesResponseDto.ReadOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<FavoritesResponseDto.ReadOneDto>> readAll(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<FavoritesResponseDto.ReadOneDto> responseDtoList
                = favoritesService.readAll(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PatchMapping("/favorites/{favoritesId}")
    @Operation(summary = "즐겨 찾기 하나 수정",
            description = "로그인한 사용자가 자신의 즐겨찾기 하나를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨 찾기 하나 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FavoritesResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "즐겨 찾기 하나 삭제",
            description = "로그인한 사용자가 자신의 즐겨찾기 하나를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "즐겨 찾기 하나 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
