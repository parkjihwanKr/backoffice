package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersResponseDto;

public interface MembersService {

    public MembersResponseDto.MembersCreateResponseDto signup(
            MembersResponseDto.MembersCreateResponseDto membersCreateResponseDto
    );


}
