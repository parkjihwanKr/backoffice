package com.example.backoffice.domain.member.repository;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByMemberName(String memberName);

    Optional<Members> findByEmailOrMemberNameOrAddressOrContact(
            String email, String memberName, String address, String contact);

    List<Members> findByDepartmentNotInAndIdNotIn(
            List<MemberDepartment> excludedDepartmentList, List<Long> excludedIdList);

    Optional<Members> findByIdAndRoleAndDepartment(
            Long memberId, MemberRole role, MemberDepartment department);

    Optional<Members> findByPositionAndDepartment(
            MemberPosition position, MemberDepartment department);

    List<Members> findAllByDepartment(MemberDepartment department);

    List<Members> findAllByPosition(MemberPosition position);

    List<Members> findAllByIdNotIn(Collection<Long> ids);
}
