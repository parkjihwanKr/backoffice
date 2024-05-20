package com.example.backoffice.domain.admin.controller;

import com.example.backoffice.domain.admin.dto.AdminResponseDto;
import com.example.backoffice.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;

    // Main Administrator가 해당 역할군 또는 부서를 바꿀 수 있게
    //
    @PatchMapping("/admin/{adminId}")
    public ResponseEntity<AdminResponseDto> updateMemberRole(
            @PathVariable Long adminId){
        adminService.updateMemberRole(adminId);
        return null;
    }
}
