package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MembersService {

    void signup(Members member);

    Members findById(Long memberId);

    Members findMember(Members member, Long memberId);

    Members validateMember(Long toMemberId, Long fromMemberId);

    Members findAdmin(
            Long adminId, MemberRole role, MemberDepartment department);

    Members findByEmailOrMemberNameOrAddressOrContact(
            String email, String memberName, String address, String contact);

    Boolean existsById(Long memberId);

    Members save(Members member);

    void deleteById(Long memberId);

    List<Members> findAllById(List<Long> excludedIdList);

    List<Members> findByMemberDepartmentNotInAndIdNotIn(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList);
}
