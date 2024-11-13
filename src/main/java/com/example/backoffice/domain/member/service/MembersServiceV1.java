package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MembersServiceV1 {

    void signup(Members member);

    Members findById(Long memberId);

    Members checkDifferentMember(Long toMemberId, Long fromMemberId);

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

    List<Members> findAllByPosition(String position);

    List<Members> findAllExceptLoginMember(Long exceptMemberId);

    void addVacationDays(Members onVacationMember, int plusVacationDays);

    void minusVacationDays(Members onVacationMember, int minusVacationDays);

    Members findHRManagerOrCEO(Members member);

    MemberDepartment findDepartment(String department);

    Page<Members> findAll(Pageable pageable);

    Page<Members> findAllByDepartment(Pageable pageable, MemberDepartment department);

    Page<Members> findAllByPosition(Pageable pageable, MemberPosition position);

    Page<Members> findAllByDepartmentAndPosition(
            MemberDepartment memberDepartment, MemberPosition position,
            Pageable pageable);

    Members findAuditManagerOrCeo(Long memberId);

    Members findByFinanceManagerOrCeo(Long memberId);

    Members findByFinanceManager();

    Members findDepartmentManager(MemberDepartment department);

    Members matchLoginMember(Members member, Long memberId);

    Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);

    Members findCeoByMemberName(String memberName);

    Members findByPosition(MemberPosition position);
}
