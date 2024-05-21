package com.example.backoffice.domain.admin.service;

import com.example.backoffice.domain.admin.converter.AdminConverter;
import com.example.backoffice.domain.admin.entity.Admin;
import com.example.backoffice.domain.admin.exception.AdminCustomException;
import com.example.backoffice.domain.admin.exception.AdminExceptionCode;
import com.example.backoffice.domain.admin.repository.AdminRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public void saveMainAdmin(Members member){
        adminRepository.save(
                AdminConverter.toEntity(member)
        );
    }

    @Override
    @Transactional
    public void updateMemberRole(Long adminId){

    }

    @Override
    @Transactional(readOnly = true)
    public Admin findById(Long adminId){
        return adminRepository.findById(adminId).orElseThrow(
                ()-> new AdminCustomException(AdminExceptionCode.NOT_FOUND_ADMIN)
        );
    }
}
