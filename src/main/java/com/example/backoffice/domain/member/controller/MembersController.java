package com.example.backoffice.domain.member.controller;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.facade.MembersServiceFacadeV1;
import com.example.backoffice.global.common.CommonResponse;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Members API", description = "직원 관리 API")
public class MembersController {

    private final MembersServiceFacadeV1 membersServiceFacade;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
    })
    public ResponseEntity<MembersResponseDto.CreateOneDto> createOneForSignup(
            @Valid @RequestBody MembersRequestDto.CreateOneDto requestDto){
        MembersResponseDto.CreateOneDto responseDto
                = membersServiceFacade.createOneForSignup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/check-available-memberName")
    @Operation(summary = "멤버 이름(접속 아이디) 중복 체크 조회", description = "새로운 회원을 등록 시에 멤버 이름을 중복 체크합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당하는 멤버 이름을 사용할 수 있습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
    })
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
    @Operation(summary = "멤버 상세보기", description = "해당하는 아이디에 맞는 멤버의 정보를 볼 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당하는 아이디의 멤버 정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.ReadOneDto.class))),
            @ApiResponse(responseCode = "400", description = "해당 멤버의 상세 정보",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MembersResponseDto.ReadOneDetailsDto> readOne(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadOneDetailsDto responseDto
                = membersServiceFacade.readOne(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/admin/members/filtered")
    @Operation(summary = "요약된 멤버들의 정보 조회", description = "관리자에 의한 멤버들의 요약된 정보 조회할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 멤버들의 요약된 정보 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MembersResponseDto.ReadOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<MembersResponseDto.ReadOneDto>> readByAdmin(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "position", required = false) String position,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable){
        Page<MembersResponseDto.ReadOneDto> responseDtoList
                = membersServiceFacade.readByAdmin(
                        department, position, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @PatchMapping("/members/{memberId}/profile")
    @Operation(summary = "멤버 한 명의 개인 정보 수정", description = "로그인 사용자의 개인 정보 수정.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 사용자의 개인 정보 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "멤버 한 명의 핵심 정보 수정",
            description = "관리자에 의해 멤버의 급여, 부서, 직책을 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당 멤버의 급여, 부서, 직책 등의 권한을 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.UpdateOneForAttributeDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<MembersResponseDto.UpdateOneForAttributeDto>> updateOneForAttributeByAdmin(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") MembersRequestDto.UpdateOneForAttributeDto requestDto,
            @RequestPart(value = "file", required = false) @Parameter(
                    description = "업로드할 파일 (이미지 또는 문서)",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary"))
            ) MultipartFile multipartFile){
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
    @Operation(summary = "멤버 한 명의 연봉 수정", description = "관리자에 의한 멤버의 연봉 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 멤버의 연봉 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.UpdateOneForSalaryDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "멤버 한 명의 프로필 사진 수정", description = "자기 자신의 프로필 사진을 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 프로필 사진 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.UpdateOneForProfileImageDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "멤버 한 명의 프로필 사진 삭제", description = "자기 자신의 프로필 사진 삭제를 할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 프로필 사진 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.DeleteOneForProfileImageDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MembersResponseDto.DeleteOneForProfileImageDto> deleteOneForProfileImage(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        MembersResponseDto.DeleteOneForProfileImageDto responseDto=
                membersServiceFacade.deleteOneForProfileImage(
                        memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/members/{memberId}")
    @Operation(summary = "멤버 한 명 삭제", description = "관리자에 의해 멤버 한 명을 삭제 할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponse<Void>> deleteOneByAdmin(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        membersServiceFacade.deleteOneByAdmin(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(HttpStatus.OK, "회원 삭제")
        );
    }

    @GetMapping("/members/{memberId}/vacations")
    @Operation(summary = "멤버 한 명의 휴가 리스트 조회",
            description = "특정 권한(사장 또는 인사 부장 또는 자기 자신)을" +
                    " 가진 인원이 멤버 한 명의 휴가 리스트를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 휴가 리스트 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.ReadOneForVacationListDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MembersResponseDto.ReadOneForVacationListDto> readOneForVacationList(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        MembersResponseDto.ReadOneForVacationListDto responseDto
                = membersServiceFacade.readOneForVacationList(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/members/{memberId}/vacations")
    @Operation(summary = "멤버 한 명의 휴가 수정",
            description = "특정 권한을 가진 인원 또는 자기 자신이 휴가를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 휴가 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MembersResponseDto.UpdateOneForVacationDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
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
    @Operation(summary = "멤버 이름 리스트 조회",
            description = "모든 멤버는 멤버 이름 리스트를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 프로필 사진 수정 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MembersResponseDto.ReadNameDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<MembersResponseDto.ReadNameDto>> readNameList(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<MembersResponseDto.ReadNameDto> responseList
                = membersServiceFacade.readNameList(memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}
