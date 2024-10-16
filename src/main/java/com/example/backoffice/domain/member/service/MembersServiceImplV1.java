package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MembersServiceImplV1 implements MembersServiceV1 {

    private final MembersRepository membersRepository;

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
    public Members checkDifferentMember(Long fromMemberId, Long toMemberId){
        if(toMemberId.equals(fromMemberId)){
            throw new MembersCustomException(MembersExceptionCode.MATCHED_LOGIN_MEMBER);
        }
        return membersRepository.findById(toMemberId).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByIdAndRoleAndDepartment(
            Long adminId, MemberRole role, MemberDepartment department){
        return membersRepository.findByIdAndRoleAndDepartment(adminId, role, department)
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
    public List<Members> findAllById(List<Long> memberIdList){
        return membersRepository.findAllById(memberIdList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findByDepartmentNotInAndIdNotIn(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList){
        return membersRepository.findByDepartmentNotInAndIdNotIn(
                excludedDepartmentList, excludedIdList);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findHRManager(){
        return membersRepository.findByPositionAndDepartment(
                MemberPosition.MANAGER, MemberDepartment.HR).orElseThrow(
                        () -> new MembersCustomException(MembersExceptionCode.NOT_FOUND_HR_MANAGER));
    }

    @Override
    @Transactional(readOnly = true)
    public Long findMemberTotalCount(){
        return membersRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByMemberName(String memberName){
        return membersRepository.findByMemberName(memberName).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAll(){
        return membersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByDepartment(MemberDepartment department){
        return membersRepository.findAllByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByPosition(MemberPosition position){
        return membersRepository.findAllByPosition(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllExceptLoginMember(Long exceptMemberId){
        return membersRepository.findAllByIdNotIn(Collections.singletonList(exceptMemberId));
    }

    @Override
    public void addVacationDays(Members onVacationMember, int plusVacationDays){
        onVacationMember.plusRemainingVacation(plusVacationDays);
    }

    @Override
    public void minusVacationDays(Members onVacationMember, int minusVacationDays){
        onVacationMember.minusRemainingVacation(minusVacationDays);
    }

    @Override
    public Members findHRManagerOrCEO(Members member){
        if ((member.getDepartment().equals(MemberDepartment.HR)
                && member.getPosition().equals(MemberPosition.MANAGER))
                || member.getPosition().equals(MemberPosition.CEO)){
            return member;
        }
        throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
    }

    @Override
    public MemberDepartment findDepartment(String department){
        return MembersConverter.toDepartment(department);
    }
}
