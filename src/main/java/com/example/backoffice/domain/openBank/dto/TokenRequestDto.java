package com.example.backoffice.domain.openBank.dto;

import lombok.Builder;
import lombok.Data;


@Data
public class TokenRequestDto {
    private String code;
    private Long memberId;

    @Builder
    public TokenRequestDto(String code, Long memberId) {
        this.code = code;
        this.memberId = memberId;
    }
}