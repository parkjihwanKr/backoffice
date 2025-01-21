package com.example.backoffice.global.audit.controller;

import com.example.backoffice.global.audit.dto.AuditLogResponseDto;
import com.example.backoffice.global.audit.service.AuditLogService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Audit Log API", description = "감사 로그 API")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/auditLogs/filtered")
    @Operation(summary = "필터링된 감사 로그 페이지 조회",
            description = "감사 부장 또는 사장이 멤버 이름, 감사 종류, 특정 날짜 범위를 필터링하여 " +
                    "감사 로그 페이지를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 감사 로그 페이지 조회 성공",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(
                                    implementation = AuditLogResponseDto.ReadOneDto.class)))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Page<AuditLogResponseDto.ReadOneDto>> readFiltered(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails,
            @RequestParam(name = "memberName", required = false) String memberName,
            @RequestParam(name = "auditType", required = false) String auditType,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<AuditLogResponseDto.ReadOneDto> responseDtoList
                = auditLogService.readFiltered(
                        memberDetails.getMembers(), memberName,
                auditType, startDate, endDate, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
