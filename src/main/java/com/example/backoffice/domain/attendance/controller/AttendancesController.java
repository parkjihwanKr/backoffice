package com.example.backoffice.domain.attendance.controller;

import com.example.backoffice.domain.attendance.dto.AttendancesRequestDto;
import com.example.backoffice.domain.attendance.dto.AttendancesResponseDto;
import com.example.backoffice.domain.attendance.service.AttendancesServiceV1;
import com.example.backoffice.global.common.DateRange;
import com.example.backoffice.global.dto.CommonResponseDto;
import com.example.backoffice.global.security.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Attendances API", description = "근태 기록 API")
public class AttendancesController {

    private final AttendancesServiceV1 attendancesService;

    @PatchMapping("/attendances/{attendanceId}/check-in")
    @Operation(summary = "멤버 한 명의 시간에 따라 상태 수정",
            description = "멤버 한 명이 특정 시간(08:30~ 10:00)에 출근을 요청, " +
                    "09:00 이내 : 정시 출근, 09:01 이후 : 지각, 10:00 이후 요청은 자동으로 결근 처리할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 근태 상태 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttendancesResponseDto.UpdateCheckInTimeDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<AttendancesResponseDto.UpdateCheckInTimeDto> updateCheckInTimeForMember(
            @PathVariable Long attendanceId,
            @RequestBody AttendancesRequestDto.UpdateCheckInTimeDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.UpdateCheckInTimeDto responseDto
                = attendancesService.updateCheckInTimeForMember(
                attendanceId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/attendances/{attendanceId}/check-out")
    @Operation(summary = "멤버 한 명의 시간에 따라 상태 수정",
            description = "멤버 한 명이 특정 시간(17:30~ 19:00)에 퇴근을 요청, " +
                    "출근 시간으로 인한 근태 상태에 따라 해당 상태가 변경됨."+
                    "17:59 이내 : 조퇴, 18:00 ~ 19:00 : 정시 출근," +
                    "19:00 이후 요청은 자동으로 결근 처리할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 한 명의 근태 상태 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttendancesResponseDto.UpdateCheckOutTimeDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<AttendancesResponseDto.UpdateCheckOutTimeDto> updateCheckOutTimeForMember(
            @PathVariable Long attendanceId,
            @RequestBody AttendancesRequestDto.UpdateCheckOutTimeDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.UpdateCheckOutTimeDto responseDto
                = attendancesService.updateCheckOutTimeForMember(
                attendanceId, requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/members/{memberId}/attendances")
    @Operation(summary = "필터링된 근태 기록 조회",
            description = "로그인한 사용자의 년, 월, 멤버 아이디에 따른 필터링 된 근태 기록 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 근태 기록 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AttendancesResponseDto.ReadOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<AttendancesResponseDto.ReadOneDto>> readFilteredForMember(
            @PathVariable Long memberId,
            @RequestParam(name = "year", required = false) Long year,
            @RequestParam(name = "month", required = false) Long month,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<AttendancesResponseDto.ReadOneDto> responseDtoList
                = attendancesService.readFilteredForMember(
                memberId, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/attendances/{attendanceId}")
    @Operation(summary = "필터링된 근태 기록 한 개 조회",
            description = "로그인한 사용자의 년, 월, 멤버 아이디에 따른 필터링 된 근태 기록 한 개 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 근태 기록 한 개 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttendancesResponseDto.ReadOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<AttendancesResponseDto.ReadOneDto> readOne(
            @PathVariable Long attendanceId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.ReadOneDto responseDto
                = attendancesService.readOne(attendanceId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/attendances")
    @Operation(summary = "관리자에 의한 필터링된 근태 기록 페이지 조회",
            description = "관리자가 멤버 이름, 근태 상태, 출근 시간, 퇴근 시간에 따른 필터링된 페이지 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 근태 기록 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AttendancesResponseDto.ReadOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<AttendancesResponseDto.ReadOneDto>> readPageByAdmin(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam(name = "memberName",required = false) String memberName,
            @RequestParam(name = "attendanceStatus", required = false) String attendanceStatus,
            @RequestParam(name = "checkInRange", required = false) @Valid DateRange checkInRange,
            @RequestParam(name = "checkOutRange", required = false) @Valid DateRange checkOutRange,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC, size = 7)
            Pageable pageable){
        Page<AttendancesResponseDto.ReadOneDto> responseDtoPage
                = attendancesService.readPageByAdmin(
                        memberName, attendanceStatus, checkInRange,
                checkOutRange, memberDetails.getMembers(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @GetMapping("/admin/attendances/monthly")
    @Operation(summary = "관리자에 의한 필터링된 월별 근태 기록 페이지 조회",
            description = "관리자가 부서, 년, 월에 따른 생성일자가 오름차순으로 되어있는 필터링된 페이지 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 필터링된 근태 기록 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AttendancesResponseDto.ReadMonthlyDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<AttendancesResponseDto.ReadMonthlyDto>> readFilteredByMonthlyByAdmin(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "year") Long year, @RequestParam(name = "month") Long month,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Page<AttendancesResponseDto.ReadMonthlyDto> responseDtoPage
                = attendancesService.readFilteredByMonthlyByAdmin(
                department, year, month, pageable, memberDetails.getMembers());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @GetMapping("/admin/attendances/daily")
    @Operation(summary = "관리자에 의한 필터링된 일별 근태 기록 페이지 조회",
            description = "관리자가 부서, 년, 월, 일에 따른 생성일자가 오름차순으로 되어있는 필터링된 페이지 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 필터링된 일별 근태 기록 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AttendancesResponseDto.ReadOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<AttendancesResponseDto.ReadOneDto>> readFilteredByDailyByAdmin(
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "memeberName", required = false) String memberName,
            @RequestParam(name = "year") Long year, @RequestParam(name = "month")Long month,
            @RequestParam(name = "day") Long day,
            @PageableDefault(sort = "memberId", direction = Sort.Direction.ASC, size = 20) Pageable pageable,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        Page<AttendancesResponseDto.ReadOneDto> responseDtoPage
                = attendancesService.readFilteredByDailyByAdmin(
                department, memberName, year, month,
                day, pageable, memberDetails.getMembers());

        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    @PatchMapping("/members/{memberId}/attendances/{attendanceId}/status")
    @Operation(summary = "관리자에 의한 근태 기록 한 개의 상태 수정",
            description = "관리자가 수동으로 특정 한 개의 근태 기록을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 근태 기록 한 개의 상태 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttendancesResponseDto.UpdateAttendancesStatusDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<AttendancesResponseDto.UpdateAttendancesStatusDto> updateOneStatusByAdmin(
            @PathVariable Long memberId, @PathVariable Long attendanceId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestBody AttendancesRequestDto.UpdateAttendanceStatusDto requestDto){
        AttendancesResponseDto.UpdateAttendancesStatusDto responseDto
                = attendancesService.updateOneStatusByAdmin(
                        memberId, attendanceId, memberDetails.getMembers(), requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/attendances")
    @Operation(summary = "관리자에 의한 특정 멤버 한 명의 근태 기록들을 생성",
            description = "관리자가 수동으로 특정 멤버 한 명의 근태 기록들을 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 특정 멤버의 근태 기록들을 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttendancesResponseDto.CreateOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<AttendancesResponseDto.CreateOneDto> createOneByAdmin(
            @RequestBody AttendancesRequestDto.CreateOneDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.CreateOneDto responseDto
                = attendancesService.createOneByAdmin(
                requestDto, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/attendances")
    @Operation(summary = "관리자에 의한 근태 기록들 삭제",
            description = "관리자가 수동으로 근태 기록의 아이디들을 통해 근태 기록들을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자에 의한 근태 기록 한 개 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<Void>> deleteByAdmin(
            @RequestBody List<Long> deleteAttendanceIdList,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        attendancesService.deleteByAdmin(
                deleteAttendanceIdList, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "해당 근태 기록들이 삭제되었습니다.", 200
                )
        );
    }

    @GetMapping("/admin/attendances")
    @Operation(summary = "관리자가 예정된 근태 기록의 일정을 조회",
            description = "관리자가 특정 멤버들의 예정 되어있는 외근 기록들의 일정 세부 사항을 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자가 예정된 근태 기록의 일정을 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AttendancesResponseDto.ReadScheduledRecordDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<AttendancesResponseDto.ReadScheduledRecordDto>> readScheduledRecordByAdmin(
            @RequestParam(name = "department", required = false) String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<AttendancesResponseDto.ReadScheduledRecordDto> responseDtoList
                = attendancesService.readScheduledRecordByAdmin(
                        department, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @GetMapping("/members/{memberId}/check-today-attendance")
    @Operation(summary = "모든 멤버가 오늘 날짜의 근태 기록을 조회",
            description = "조회가 많이 이루어지는 오늘 하루의 근태 기록을 캐싱 데이터를 활용하기 위한 조회.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 멤버가 오늘 날짜의 근태 기록을 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttendancesResponseDto.ReadTodayOneDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<AttendancesResponseDto.ReadTodayOneDto> readTodayOne (
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        AttendancesResponseDto.ReadTodayOneDto responseDto
                = attendancesService.readTodayOne(memberId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}