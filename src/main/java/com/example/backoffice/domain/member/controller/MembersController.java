package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MembersController {

    private final MembersService membersService;

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MembersResponseDto> readMemberInfo(){
        return null;
    }

    @PostMapping("/members")
    public ResponseEntity<MembersResponseDto> signup(
            @RequestBody MembersResponseDto.MembersCreateResponseDto membersCreateResponseDto){
        membersService.signup(membersCreateResponseDto);
        return ResponseEntity.noContent().build();
    }
}
