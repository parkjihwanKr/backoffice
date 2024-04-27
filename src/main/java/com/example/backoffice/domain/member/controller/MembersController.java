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

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<Void>> login(
            @RequestBody MembersRequestDto.LoginMemberRequestDto requestDto){
        membersService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "로그인 성공!")
        );
    }

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.ReadMemberResponseDto> readMemberInfo(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadMemberResponseDto responseDto = membersService.readMemberInfo(memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.UpdateMemberResponseDto> updateMember(
            @PathVariable long memberId, @RequestBody MembersRequestDto.UpdateMemberRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.UpdateMemberResponseDto responseDto
                = membersService.updateMember(memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}/role")
    public ResponseEntity<MembersResponseDto.UpdateMemberRoleResponseDto> updateRole(
            @PathVariable long memberId, MembersRequestDto.UpdateMemberRoleRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam("file")MultipartFile file){
        MembersResponseDto.UpdateMemberRoleResponseDto responseDto =
                membersService.updateMemberRole(memberId, memberDetails.getMembers(), requestDto, file);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.UpdateMemberProfileImageUrlResponseDto> updateProfile(
            @PathVariable long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam("file")MultipartFile image){
        MembersResponseDto.UpdateMemberProfileImageUrlResponseDto responseDto =
                membersService.updateMemberProfileImageUrl(memberId, memberDetails.getMembers(), image);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.DeleteMemberProfileImageResponseDto> deleteProfile(
            @PathVariable Long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        MembersResponseDto.DeleteMemberProfileImageResponseDto responseDto=
                membersService.deleteMemberProfileImage(memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponse<Void>> deleteMember(
            @PathVariable long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersService.deleteMember(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "회원 삭제")
        );
    }
}
