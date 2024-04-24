package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MembersController {

    private final MembersService membersService;

    @PostMapping("/signup")
    public ResponseEntity<MembersResponseDto.CreateMembersResponseDto> signup(
            @Valid @RequestBody MembersRequestDto.CreateMembersRequestDto requestDto){
        MembersResponseDto.CreateMembersResponseDto responseDto
                = membersService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 로그인 로직이 없네...? 그냥 filter에서 처리하는듯?
    // MemberDetails에 대한 정보가 그래서 없나봄
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody MembersRequestDto.LoginMemberRequestDto requestDto
            /*@AuthenticationPrincipal MemberDetailsImpl memberDetails*/){
        membersService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
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

    @PatchMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.UpdateMemberProfileImageUrlResponseDto> updateProfileUrl(
            @PathVariable long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            MultipartFile image){
        MembersResponseDto.UpdateMemberProfileImageUrlResponseDto responseDto =
                membersService.updateMemberProfileImageUrl(memberId, memberDetails.getMembers(), image);
        return ResponseEntity.ok(responseDto);
    }
    @DeleteMapping("/members/{meberId}")
    public void deleteMember(
            @PathVariable long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersService.deleteMember(memberId, memberDetails.getMembers());
    }
}
