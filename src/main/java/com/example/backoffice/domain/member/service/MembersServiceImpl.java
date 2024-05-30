package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{

    private final MembersRepository membersRepository;

    // 타당성 검사 추가
    @Override
    @Transactional
    public void signup(Members member){
        membersRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findById(Long memberId){
        return membersRepository.findById(memberId).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Members findMember(Members member, Long memberId){
        if(!member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_INFO);
        }
        return findById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Members validateMember(Long toMemberId, Long fromMemberId){
        if(toMemberId.equals(fromMemberId)){
            throw new MembersCustomException(MembersExceptionCode.MATCHED_LOGIN_MEMBER);
        }
        return membersRepository.findById(toMemberId).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByIdAndRoleAndMemberDepartment(
            Long adminId, MemberRole role, MemberDepartment department){
        return membersRepository.findByIdAndRoleAndMemberDepartment(adminId, role, department)
                .orElseThrow(
                        ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByEmailOrMemberNameOrAddressOrContact(
            String email, String memberName, String address, String contact){
        return membersRepository.findByEmailOrMemberNameOrAddressOrContact(
                email, memberName, address, contact).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(Long memberId){
        return membersRepository.existsById(memberId);
    }

    @Override
    @Transactional
    public Members save(Members member){
        return membersRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteById(Long memberId){
        membersRepository.deleteById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllById(List<Long> excludedIdList){
        return membersRepository.findAllById(excludedIdList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findByMemberDepartmentNotInAndIdNotIn(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList){
        return membersRepository.findByMemberDepartmentNotInAndIdNotIn(
                excludedDepartmentList, excludedIdList);
    }
}
