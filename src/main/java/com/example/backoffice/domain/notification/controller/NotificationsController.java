package com.example.backoffice.domain.notification.controller;

import com.example.backoffice.domain.notification.dto.NotificationsRequestDto;
import com.example.backoffice.domain.notification.dto.NotificationsResponseDto;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Notifications API", description = "알림 API")
public class NotificationsController {

    private final NotificationsServiceFacadeV1 notificationsServiceFacade;

    // 알림 단건 조회
    @GetMapping("/members/{memberId}/notifications/{notificationId}")
    @Operation(summary = "알림 한 개 조회",
            description = "로그인한 사용자는 자신의 알림을 하나 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 한 개 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<NotificationsResponseDto.ReadOneDto>> readOne(
            @PathVariable Long memberId, @PathVariable String notificationId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        NotificationsResponseDto.ReadOneDto responseDto
                = notificationsServiceFacade.readOne(memberId, notificationId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDto, "알림 단건 조회 성공", 200
                )
        );
    }
    // 알림 삭제
    @DeleteMapping("/members/{memberId}/notifications")
    @Operation(summary = "알림 삭제",
            description = "로그인한 사용자는 자신의 알림을 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<List<String>>> delete(
            @PathVariable Long memberId,
            @RequestBody NotificationsRequestDto.DeleteDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        List<String> deleteIdList = notificationsServiceFacade.delete(
                memberId, requestDto, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        deleteIdList, "알림 삭제 성공", 200));
    }

    // 알림 리스트 조회
    @GetMapping("/members/{memberId}/notifications")
    @Operation(summary = "알림 페이지 조회",
            description = "로그인한 사용자는 자신의 알림 페이지 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = NotificationsResponseDto.ReadDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<NotificationsResponseDto.ReadDto>> read(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NotificationsResponseDto.ReadDto> responseDto
                = notificationsServiceFacade.read(memberId, memberDetails.getMembers(), pageable);
        return ResponseEntity.ok().body(responseDto);
    }

    // 모든 알림 리스트를 '읽음' 버튼으로 모두 읽음으로 변경
    @PostMapping("/members/{memberId}/notifications/change-is-read-true")
    @Operation(summary = "알림 리스트를 '읽음' 상태로 수정",
            description = "로그인한 사용자는 자신의 알림 리스트를 '읽음' 상태로 수정할 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 리스트를 '읽음' 상태로 수정 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = NotificationsResponseDto.ReadSummaryOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CommonResponseDto<List<NotificationsResponseDto.ReadSummaryOneDto>>> changeIsReadTrue(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        List<NotificationsResponseDto.ReadSummaryOneDto> responseDtoList
                = notificationsServiceFacade.changeIsReadTrue(memberId, memberDetails.getMembers());
        return ResponseEntity.ok().body(
                new CommonResponseDto<>(
                        responseDtoList, "모든 알림 리스트 읽기 성공", 200
                )
        );
    }
}
