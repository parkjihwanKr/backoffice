package com.example.backoffice.domain.member.repository;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Optional<Members> findByPositionAndDepartment(
            MemberPosition position, MemberDepartment department);

    Page<Members> findAllByDepartmentAndPosition(
            MemberDepartment department, MemberPosition position, Pageable pageable);

    List<Members> findAllByDepartment(MemberDepartment department);

    List<Members> findAllByIdNotIn(Collection<Long> ids);

    Page<Members> findAllByDepartment(Pageable pageable, MemberDepartment department);

    Page<Members> findAllByPosition(Pageable pageable, MemberPosition position);

    Optional<Members> findByPosition(MemberPosition position);

    List<Members> findAllByMemberName(String memberName);

    List<Members> findAllByDepartmentAndMemberName(
            MemberDepartment department, String memberName);

    Optional<Members> findByIdAndDepartment(
            Long memberId, MemberDepartment department);

    Boolean existsByMemberName(String memberName);

    List<Members> findByDepartmentNotIn(List<MemberDepartment> excludedDepartmentList);

    List<Members> findByIdNotIn(List<Long> excludedIdList);
}
