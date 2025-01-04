package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MembersController {

    private final MembersServiceFacadeV1 membersServiceFacade;

    @PostMapping("/signup")
    public ResponseEntity<MembersResponseDto.CreateOneDto> createOneForSignup(
            @Valid @RequestBody MembersRequestDto.CreateOneDto requestDto){
        MembersResponseDto.CreateOneDto responseDto
                = membersServiceFacade.createOneForSignup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/check-available-memberName")
    public ResponseEntity<CommonResponse<MembersResponseDto.ReadAvailableMemberNameDto>> checkAvailableMemberName(
            @RequestParam(name = "memberName") String memberName){
        MembersResponseDto.ReadAvailableMemberNameDto responseDto
                = membersServiceFacade.checkAvailableMemberName(memberName);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        200, "해당 아이디는 사용 가능합니다.", responseDto
                )
        );
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MembersResponseDto.ReadOneDetailsDto> readOne(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadOneDetailsDto responseDto
                = membersServiceFacade.readOne(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/admin/members/filtered")
    public ResponseEntity<Page<MembersResponseDto.ReadOneDto>> readByAdmin(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "position", required = false) String position,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC, size = 10) Pageable pageable){
        Page<MembersResponseDto.ReadOneDto> responseDtoList
                = membersServiceFacade.readByAdmin(
                        department, position, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PatchMapping("/members/{memberId}/profile")
    public ResponseEntity<MembersResponseDto.UpdateOneDto> updateOne(
            @PathVariable(name = "memberId") Long memberId,
            @RequestBody MembersRequestDto.UpdateOneDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.UpdateOneDto responseDto
                = membersServiceFacade.updateOne(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/members/{memberId}/attribute")
    public ResponseEntity<CommonResponse<MembersResponseDto.UpdateOneForAttributeDto>> updateOneForAttributeByAdmin(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") MembersRequestDto.UpdateOneForAttributeDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile){
        MembersResponseDto.UpdateOneForAttributeDto responseDto =
                membersServiceFacade.updateOneForAttributeByAdmin(
                        memberId, memberDetails.getMembers(), requestDto, multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        HttpStatus.OK, "해당 사항이 변경되었습니다.", responseDto
                )
        );
    }

    // 급여 변경
    @PatchMapping("/members/{memberId}/attribute/salary")
    public ResponseEntity<MembersResponseDto.UpdateOneForSalaryDto> updateOneForSalaryByAdmin(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody MembersRequestDto.UpdateOneForSalaryDto requestDto){
        MembersResponseDto.UpdateOneForSalaryDto responseDto =
                membersServiceFacade.updateOneForSalaryByAdmin(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/members/{memberId}/profile-image")
    public ResponseEntity<MembersResponseDto.UpdateOneForProfileImageDto> updateOneForProfileImage(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam(name = "file")MultipartFile image){
        MembersResponseDto.UpdateOneForProfileImageDto responseDto =
                membersServiceFacade.updateOneForProfileImage(
                        memberId, memberDetails.getMembers(), image);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/members/{memberId}/profile-image")
    public ResponseEntity<MembersResponseDto.DeleteOneForProfileImageDto> deleteOneForProfileImage(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        MembersResponseDto.DeleteOneForProfileImageDto responseDto=
                membersServiceFacade.deleteOneForProfileImage(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponse<Void>> deleteOneByAdmin(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersServiceFacade.deleteOneByAdmin(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "회원 삭제")
        );
    }

    @GetMapping("/members/{memberId}/vacations")
    public ResponseEntity<MembersResponseDto.ReadOneForVacationListDto> readOneForVacationList(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadOneForVacationListDto responseDto
                = membersServiceFacade.readOneForVacationList(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/members/{memberId}/vacations")
    public ResponseEntity<MembersResponseDto.UpdateOneForVacationDto> updateMemberVacationByAdmin(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody MembersRequestDto.UpdateOneForVacationDto requestDto){
        MembersResponseDto.UpdateOneForVacationDto responseDto
                = membersServiceFacade.updateMemberVacationByAdmin(
                        memberId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/members/nameList")
    public ResponseEntity<List<MembersResponseDto.ReadNameDto>> readNameList(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<MembersResponseDto.ReadNameDto> responseList
                = membersServiceFacade.readNameList(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/members/{memberId}/profile-image")
    public ResponseEntity<MembersResponseDto.ReadOneForProfileImageDto> readOneForProfileImage(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        MembersResponseDto.ReadOneForProfileImageDto responseDto
                = membersServiceFacade.readOneForProfileImage(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
