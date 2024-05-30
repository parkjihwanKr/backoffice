package com.example.backoffice.domain.member.repository;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByMemberName(String memberName);

    Optional<Members> findByEmailOrMemberNameOrAddressOrContact(
            String email, String memberName, String address, String contact);

    List<Members> findByMemberDepartmentNotInAndIdNotIn(
            List<MemberDepartment> excludedDepartmentList, List<Long> excludedIdList);

    Optional<Members> findByIdAndRoleAndMemberDepartment(
            Long memberId, MemberRole role, MemberDepartment department);
}
