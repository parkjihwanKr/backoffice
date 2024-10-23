package com.example.backoffice.domain.asset.service;

import com.example.backoffice.domain.asset.dto.AssetResponseDto;
import com.example.backoffice.domain.member.entity.Members;

public interface AssetServiceV1 {

    AssetResponseDto.ReadOneDto readOne(Members loginMember);
}
