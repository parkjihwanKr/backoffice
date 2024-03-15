package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.global.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MembersController {

    private final MembersService membersService;

    @PostMapping("/signup")
    public ResponseEntity<MembersResponseDto.CreateMembersResponseDto> signup(
            @RequestBody MembersRequestDto.CreateMembersRequestDto requestDto){
        MembersResponseDto.CreateMembersResponseDto responseDto = membersService.signup(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody MembersRequestDto.LoginMemberRequestDto requestDto){
        membersService.login(requestDto);
    }

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.ReadMemberResponseDto> readMemberInfo(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadMemberResponseDto responseDto = membersService.readMemberInfo(memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<MembersResponseDto.UpdateMemberResponseDto> updateMember(
            @PathVariable long memberId, @RequestBody MembersRequestDto.UpdateMemberRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.UpdateMemberResponseDto responseDto
                = membersService.updateMember(memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}/role")
    public ResponseEntity<MembersResponseDto.UpdateMemberRoleResponseDto> updateRole(
            @PathVariable long memberId, @RequestBody MembersRequestDto.UpdateMemberRoleRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.UpdateMemberRoleResponseDto responseDto =
                membersService.updateMemberRole(memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{meberId}")
    public void deleteMember(
            @PathVariable long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersService.deleteMember(memberId, memberDetails.getMembers());
    }
}
