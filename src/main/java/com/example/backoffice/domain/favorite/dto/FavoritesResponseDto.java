package com.example.backoffice.domain.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FavoritesResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FavoritesResponseDto.CreateOneDto",
            description = "즐겨찾기 하나 생성 응답 DTO")
    public static class CreateOneDto{
        private Long favoritesId;
        private String favoritesUrl;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FavoritesResponseDto.UpdateOneDto",
            description = "즐겨찾기 하나 수정 응답 DTO")
    public static class UpdateOneDto {
        private Long favoritesId;
        private String favoritesUrl;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FavoritesResponseDto.ReadOneDto",
            description = "즐겨찾기 하나 조회 응답 DTO")
    public static class ReadOneDto{
        private Long favoritesId;
        private String favoritesUrl;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FavoritesResponseDto.ReadSummaryOneDto",
            description = "요약된 즐겨찾기 하나 조회 응답 DTO")
    public static class ReadSummaryOneDto {
        private Long favoritesId;
        private String favoritesUrl;
        private String description;
    }
}
