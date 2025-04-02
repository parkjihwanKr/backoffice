package com.example.backoffice.domain.vacation.controller;

import com.example.backoffice.domain.vacation.dto.VacationsRequestDto;
import com.example.backoffice.domain.vacation.dto.VacationsResponseDto;
import com.example.backoffice.domain.vacation.facade.VacationsServiceFacadeV1;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Vacations API", description = "휴가 API")
public class VacationsController {

    private final VacationsServiceFacadeV1 vacationsServiceFacade;
    
    @PatchMapping("/vacations/update-period")
    @Operation(summary = "월별 휴가 신청 기간 수정",
            description = "인사 부장 또는 사장에 의해 이례적으로 휴가 신청 기간이 변경할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월별 휴가 신청 기간 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.UpdatePeriodDto>> updatePeriodByAdmin(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.UpdatePeriodDto requestDto){
        VacationsResponseDto.UpdatePeriodDto responseDto
                = vacationsServiceFacade.updatePeriodByAdmin(memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, "성공적으로 휴가 신청 기간이 변경되었습니다.", 200));
    }

    @GetMapping("/vacations/update-period")
    @Operation(summary = "월별 휴가 신청 기간 조회",
            description = "로그인한 사용자는 휴가 신청 기간의 상세정보를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월별 휴가 신청 기간 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacationsResponseDto.ReadPeriodDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<VacationsResponseDto.ReadPeriodDto> readUpcomingUpdateVacationPeriod(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        VacationsResponseDto.ReadPeriodDto responseDto
                = vacationsServiceFacade.readUpcomingUpdateVacationPeriod(
                        memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    
    @PostMapping("/vacations")
    @Operation(summary = "휴가 생성",
            description = "로그인한 사용자는 유효한 날짜의 휴가를 신청할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "휴가 신청 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.CreateOneDto>> createOneByMember(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.CreateOneDto requestDto){
        VacationsResponseDto.CreateOneDto responseDto =
                vacationsServiceFacade.createOneByMember(memberDetails.getMembers(), requestDto);
        String message = "해당 사항은 검토 후, 사내 알림으로 알려드리겠습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, message, 200));
    }

    @GetMapping("/vacations/{vacationId}")
    @Operation(summary = "휴가 조회",
            description = "로그인한 사용자는 특정날에 신청한 휴가를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "휴가 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacationsResponseDto.ReadDayDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<VacationsResponseDto.ReadDayDto> readDayByMember(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        VacationsResponseDto.ReadDayDto responseDto
                = vacationsServiceFacade.readDayByMember(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/vacations/{vacationId}")
    @Operation(summary = "휴가 신청 수정",
            description = "로그인한 사용자는 승인 신청이 나지 않은 자신의 휴가를 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "휴가 신청 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacationsResponseDto.UpdateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<VacationsResponseDto.UpdateOneDto> updateOneByMember(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody VacationsRequestDto.UpdateOneDto requestDto){
        VacationsResponseDto.UpdateOneDto responseDto
                = vacationsServiceFacade.updateOneByMember(vacationId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회사원들의 휴가를 적용 여부를 변경
    @PatchMapping("/admin/vacations/{vacationId}")
    @Operation(summary = "관리자에 의한 휴가 신청 수정",
            description = "인사 부장 또는 사장이 멤버들의 휴가 신청을 승인 또는 거부 할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 휴가 신청 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<VacationsResponseDto.UpdateOneByAdminDto>> updateOneByAdmin(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        VacationsResponseDto.UpdateOneByAdminDto responseDto
                = vacationsServiceFacade.updateOneByAdmin(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        responseDto, "승인 요청이 되었습니다.", 200
                )
        );
    }

    // 개인 휴가 일정 부분 삭제
    @DeleteMapping("/vacations/{vacationId}")
    @Operation(summary = "휴가 신청 삭제",
            description = "로그인한 사용자는 승인 신청이 나지 않은 자신의 휴가를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "휴가 신청 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<Void>> deleteOneByMember(
            @PathVariable Long vacationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        vacationsServiceFacade.deleteOneByMember(vacationId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "휴가 등록 취소 성공", 200
                )
        );
    }

    // 특정 달의 필터링된 휴가 상황 모두 조회
    // readByHrManager
    @GetMapping("/vacations/years/{year}/months/{month}/filtered")
    @Operation(summary = "필터링된 월별 휴가 리스트 조회",
            description = "인사부장 또는 사장은 요약된 월별 멤버들의 휴가 리스트를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 월별 휴가 리스트 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = VacationsResponseDto.ReadMonthDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<VacationsResponseDto.ReadMonthDto>> readByHrManager(
            @PathVariable Long year, @PathVariable Long month,
            @RequestParam(name = "isAccepted", required = false) Boolean isAccepted,
            @RequestParam(name = "urgent", required = false) Boolean urgent,
            @RequestParam(name = "department", required = false) String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<VacationsResponseDto.ReadMonthDto> responseDtoList
                = vacationsServiceFacade.readByHrManager(
                        year, month, isAccepted, urgent, department, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @DeleteMapping("/admin/vacations/{vacationId}")
    @Operation(summary = "관리자에 의한 멤버 휴가 삭제",
            description = "인사 부장 또는 사장은 특정 사유로 적어서 해당 멤버에게 휴가를 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 멤버 휴가 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<Void>> deleteOneByHrManager(
            @PathVariable Long vacationId,
            @RequestBody VacationsRequestDto.DeleteOneByAdminDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        vacationsServiceFacade.deleteOneByHrManager(
                vacationId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "성공적으로 휴가 요청이 삭제되었습니다.", 200
                )
        );
    }
}
