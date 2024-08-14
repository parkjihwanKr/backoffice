package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;

import java.util.List;

public interface MembersServiceV1 {

    void signup(Members member);

    Members findById(Long memberId);

    Members readOneForDifferentMemberCheck(Long toMemberId, Long fromMemberId);

    Members findByEmailOrMemberNameOrAddressOrContact(
            String email, String memberName, String address, String contact);

    Boolean existsById(Long memberId);

    Members save(Members member);

    void deleteById(Long memberId);

    List<Members> findAllById(List<Long> memberIdList);

    List<Members> findByDepartmentNotInAndIdNotIn(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    Members findByIdAndRoleAndDepartment(
            Long adminId, MemberRole role, MemberDepartment department);

    Members findHRManager();

    Long findMemberTotalCount();

    Members findByMemberName(String memberName);

    List<Members> findAll();

    List<Members> findAllByDepartment(MemberDepartment department);

    List<Members> findAllByPosition(MemberPosition position);
}
