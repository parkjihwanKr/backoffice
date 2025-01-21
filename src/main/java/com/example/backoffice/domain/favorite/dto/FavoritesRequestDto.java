package com.example.backoffice.domain.favorite.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FavoritesRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FavoritesRequestDto.CreateOneDto",
            description = "즐겨찾기 하나 생성 요청 DTO")
    public static class CreateOneDto {
        private String url;
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "FavoritesRequestDto.UpdateOneDto",
            description = "즐겨찾기 하나 수정 요청 DTO")
    public static class UpdateOneDto {
        private String description;
    }
}
