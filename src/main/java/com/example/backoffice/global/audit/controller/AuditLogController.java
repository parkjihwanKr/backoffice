package com.example.backoffice.global.audit.controller;

import com.example.backoffice.global.audit.dto.AuditLogResponseDto;
import com.example.backoffice.global.audit.service.AuditLogService;
import com.example.backoffice.global.security.MemberDetailsImpl;
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
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/auditLogs/filtered")
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
