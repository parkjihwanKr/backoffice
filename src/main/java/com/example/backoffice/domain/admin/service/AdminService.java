package com.example.backoffice.domain.admin.service;

import com.example.backoffice.domain.admin.entity.Admin;
import com.example.backoffice.domain.member.entity.Members;

public interface AdminService {

    void saveMainAdmin(Members member);

    void updateMemberRole(Long adminId);

    Admin findById(Long adminId);
}
