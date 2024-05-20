package com.example.backoffice.domain.admin.service;

import com.example.backoffice.domain.member.entity.Members;

public interface AdminService {

    public void saveMainAdmin(Members member);

    public void updateMemberRole(Long adminId);
}
