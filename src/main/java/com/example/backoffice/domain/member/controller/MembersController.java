package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.fascade.MembersServiceFacade;
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

    private final MembersServiceFacade membersServiceFacade;

    @PostMapping("/signup")
    public ResponseEntity<MembersResponseDto.CreateMembersResponseDto> signup(
            @Valid @RequestBody MembersRequestDto.CreateMembersRequestDto requestDto){
        MembersResponseDto.CreateMembersResponseDto responseDto
                = membersServiceFacade.signup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.ReadMemberResponseDto> readInfo(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadMemberResponseDto responseDto
                = membersServiceFacade.readInfo(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.UpdateMemberResponseDto> updateMember(
            @PathVariable Long memberId, @RequestBody MembersRequestDto.UpdateMemberRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.UpdateMemberResponseDto responseDto
                = membersServiceFacade.updateMember(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 부서, 권한, 직위를 전부 다 바꿀 수 있게 하는건? -> null이여도 상관없게
    // @ModelAttribute 사용하기
    @PatchMapping("/members/{memberId}/attribute")
    public ResponseEntity<CommonResponse<MembersResponseDto.UpdateMemberAttributeResponseDto>> updateAttribute(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @ModelAttribute MembersRequestDto.UpdateMemberAttributeRequestDto requestDto){
        MembersResponseDto.UpdateMemberAttributeResponseDto responseDto =
                membersServiceFacade.updateAttribute(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "해당 사항이 변경되었습니다.", responseDto
                )
        );
    }

    // 급여 변경
    @PatchMapping("/members/{memberId}/attribute/salary")
    public ResponseEntity<MembersResponseDto.UpdateMemberSalaryResponseDto> updateSalary(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody MembersRequestDto.UpdateMemberSalaryRequestDto requestDto){
        MembersResponseDto.UpdateMemberSalaryResponseDto responseDto =
                membersServiceFacade.updateSalary(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 휴가 요청
    @PatchMapping("/members/{memberId}/vacation")
    public ResponseEntity<CommonResponse<MembersResponseDto.UpdateMemberVacationDaysResponseDto>> updateVactionDays(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody MembersRequestDto.UpdateMemberVacationDaysRequestDto requestDto){
        MembersResponseDto.UpdateMemberVacationDaysResponseDto responseDto =
                membersServiceFacade.updateVacationDays(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "검토 후 변경 예정입니다.", responseDto
                )
        );
    }

    @PatchMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.UpdateMemberProfileImageUrlResponseDto> updateProfile(
            @PathVariable Long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam("file")MultipartFile image){
        MembersResponseDto.UpdateMemberProfileImageUrlResponseDto responseDto =
                membersServiceFacade.updateProfileImageUrl(
                        memberId, memberDetails.getMembers(), image);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}/profileImage")
    public ResponseEntity<MembersResponseDto.DeleteMemberProfileImageResponseDto> deleteProfile(
            @PathVariable Long memberId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        MembersResponseDto.DeleteMemberProfileImageResponseDto responseDto=
                membersServiceFacade.deleteProfileImage(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponse<Void>> deleteMember(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersServiceFacade.deleteMember(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "회원 삭제")
        );
    }
}
