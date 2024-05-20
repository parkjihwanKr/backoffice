package com.example.backoffice.domain.admin.converter;

import com.example.backoffice.domain.admin.entity.Admin;
import com.example.backoffice.domain.admin.entity.AdminRole;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;

public class AdminConverter {

    public static Admin toEntity(Members member){
        return Admin.builder()
                .member(member)
                .role(AdminRole.MAIN_ADMIN)
                .build();
    }
}
