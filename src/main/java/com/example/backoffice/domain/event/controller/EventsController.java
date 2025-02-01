package com.example.backoffice.domain.event.controller;

import com.example.backoffice.domain.event.dto.EventsRequestDto;
import com.example.backoffice.domain.event.dto.EventsResponseDto;
import com.example.backoffice.domain.event.facade.EventsServiceFacadeV1;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Events API", description = "일정 API")
public class EventsController {

    private final EventsServiceFacadeV1 eventsServiceFacade;

    @GetMapping("/departments/{department}/events/years/{year}/months/{month}")
    @Operation(summary = "월별 부서 타입 일정 한 개 조회",
            description = "로그인한 사용자의 부서와 일치하는 부서 일정을 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월별 부서 타입 일정 한 개 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = EventsResponseDto.ReadOneDepartmentTypeDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<EventsResponseDto.ReadOneDepartmentTypeDto>> readForDepartmentMonthEvent(
            @PathVariable String department, @PathVariable Long year,
            @PathVariable Long month, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadOneDepartmentTypeDto> responseDtoList
                = eventsServiceFacade.readForDepartmentMonthEvent(
                        department, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 부서 일정 생성
    @PostMapping(
            value = "/departments/{department}/events",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "부서 타입 일정 한 개 생성",
            description = "로그인 사용자와 같은 부서이거나 특정 권한을 가진 멤버는 부서 타입의 일정을 생성할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 일정 한 개 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    EventsResponseDto.CreateOneDepartmentTypeDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EventsResponseDto.CreateOneDepartmentTypeDto> createOneDepartmentType(
            @PathVariable String department,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid EventsRequestDto.CreateOneDepartmentTypeDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files){
        EventsResponseDto.CreateOneDepartmentTypeDto responseDto
                = eventsServiceFacade.createOneDepartmentType(
                        department, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 부서 일정 부분 수정
    @PatchMapping(
            value = "/departments/{department}/events/{eventId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "부서 타입 일정 한 개 수정",
            description = "로그인 사용자와 같은 부서이거나 특정 권한을 가진 멤버는 부서 타입의 일정을 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 일정 한 개 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    EventsResponseDto.UpdateOneDepartmentTypeDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<EventsResponseDto.UpdateOneDepartmentTypeDto> updateOneDepartmentType(
            @PathVariable String department, @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestPart(value = "data") @Valid EventsRequestDto.UpdateOneDepartmentTypeDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files){
        EventsResponseDto.UpdateOneDepartmentTypeDto responseDto
                = eventsServiceFacade.updateOneDepartmentType(
                        department, eventId, memberDetails.getMembers(), requestDto, files);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/departments/{department}/events/{eventId}")
    @Operation(summary = "부서 타입 일정 한 개 삭제",
            description = "로그인 사용자와 같은 부서이거나 특정 권한을 가진 멤버는 부서 타입의 일정을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부서 타입 일정 한 개 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<Void>> deleteOneDepartmentType(
            @PathVariable String department, @PathVariable Long eventId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        eventsServiceFacade.deleteOneDepartmentType(
                department, eventId, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponseDto<>(
                        null, "부서 일정 삭제 성공", 200
                )
        );
    }

    @GetMapping("/members/{memberId}/events/years/{year}/month/{month}")
    @Operation(summary = "월별 개인 일정 조회",
            description = "로그인 사용자는 자신의 년, 월에 대한 개인 일정표를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월별 개인 일정 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = EventsResponseDto.ReadOnePersonalScheduleDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<EventsResponseDto.ReadOnePersonalScheduleDto>> readMonthlyPersonalSchedule(
            @PathVariable Long memberId, @PathVariable Long year,
            @PathVariable Long month, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadOnePersonalScheduleDto> responseDtoList
                = eventsServiceFacade.readMonthlyPersonalSchedule(
                        memberId, year, month, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 개인 일정 1일 조회
    @GetMapping("/members/{memberId}/events/years/{year}/months/{month}/days/{day}")
    @Operation(summary = "특정일의 개인 일정 상세보기 조회",
            description = "로그인 사용자는 개인 일정표의 특정 일을 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "특정일의 개인 일정 상세보기 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = EventsResponseDto.ReadOnePersonalScheduleDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<EventsResponseDto.ReadOnePersonalScheduleDto>> readDailyPersonalSchedule(
            @PathVariable Long memberId, @PathVariable Long year,
            @PathVariable Long month, @PathVariable Long day,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        List<EventsResponseDto.ReadOnePersonalScheduleDto> responseDtoList
                = eventsServiceFacade.readDailyPersonalSchedule(
                        memberId, year, month, day, memberDetails.getMembers());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
