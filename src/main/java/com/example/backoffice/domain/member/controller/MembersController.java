package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
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

    private final MembersServiceFacadeV1 membersServiceFacade;

    @PostMapping("/signup")
    public ResponseEntity<MembersResponseDto.CreateOneDto> signup(
            @Valid @RequestBody MembersRequestDto.CreateOneDto requestDto){
        MembersResponseDto.CreateOneDto responseDto
                = membersServiceFacade.signup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.ReadOneProfileDto> readOneProfile(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadOneProfileDto responseDto
                = membersServiceFacade.readOneProfile(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.UpdateOneProfileDto> updateOneProfile(
            @PathVariable Long memberId, @RequestBody MembersRequestDto.UpdateOneProfileDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.UpdateOneProfileDto responseDto
                = membersServiceFacade.updateOneProfile(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 부서, 권한, 직위를 전부 다 바꿀 수 있게 하는건? -> null이여도 상관없게
    // @ModelAttribute 사용하기
    @PatchMapping("/members/{toMemberId}/attribute")
    public ResponseEntity<CommonResponse<MembersResponseDto.UpdateOneAttributeDto>> updateOneAttribute(
            @PathVariable Long toMemberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @ModelAttribute MembersRequestDto.UpdateOneAttributeDto requestDto){
        MembersResponseDto.UpdateOneAttributeDto responseDto =
                membersServiceFacade.updateOneAttribute(
                        toMemberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "해당 사항이 변경되었습니다.", responseDto));
    }

    // 급여 변경
    @PatchMapping("/members/{toMemberId}/attribute/salary")
    public ResponseEntity<MembersResponseDto.UpdateOneSalaryDto> updateOneSalary(
            @PathVariable Long toMemberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody MembersRequestDto.UpdateOneSalaryDto requestDto){
        MembersResponseDto.UpdateOneSalaryDto responseDto =
                membersServiceFacade.updateOneSalary(
                        toMemberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.UpdateOneProfileImageDto> updateOneProfileImage(
            @PathVariable Long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam("file")MultipartFile image){
        MembersResponseDto.UpdateOneProfileImageDto responseDto
                = membersServiceFacade.updateOneProfileImage(
                        memberId, memberDetails.getMembers(), image);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.DeleteOneProfileImageDto> deleteOneProfile(
            @PathVariable Long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        MembersResponseDto.DeleteOneProfileImageDto responseDto
                = membersServiceFacade.deleteOneProfileImage(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponse<Void>> deleteOne(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersServiceFacade.deleteOne(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "회원 삭제 성공")
        );
    }
}
