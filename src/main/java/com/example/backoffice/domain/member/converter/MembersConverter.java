package com.example.backoffice.domain.member.converter;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;

public class MembersConverter {

    public static Members toAdminEntity(String bcrytPassword){
        return Members.builder()
                .memberName("admin")
                .name("admin")
                .loveCount(0L)
                .role(MemberRole.ADMIN)
                .email("admin@test.com")
                .address("admin시 admin동")
                .introduction("admin이다")
                .department(MemberDepartment.HR)
                .password(bcrytPassword)
                .contact("010-0000-0000")
                .position(MemberPosition.CEO)
                .remainingVacationDays(4)
                .onVacation(false)
                .salary(20000000L)
                .build();
    }
    public static Members toEntity(
            MembersRequestDto.CreateOneDto requestDto, String bcryptPassword) {
        return Members.builder()
                .memberName(requestDto.getMemberName())
                .name(requestDto.getName()) // 이름을 name으로 설정하는 것이 맞는지 확인
                .role(MemberRole.EMPLOYEE) // 역할 설정, MemberRole.USER 또는 직접 설정
                .department(MemberDepartment.HR)
                .position(MemberPosition.INTERN)
                .email(requestDto.getEmail())
                .address(requestDto.getAddress())
                .loveCount(0L)
                .password(bcryptPassword) // 암호화된 비밀번호 사용
                .contact(requestDto.getContact())
                .remainingVacationDays(4)
                .salary(24000000L)
                .onVacation(false)
                .build();
    }

    public static MembersResponseDto.CreateOneDto toCreateOneDto(Members member){
        return MembersResponseDto.CreateOneDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .memberName(member.getMemberName())
                .name(member.getMemberName())
                .contact(member.getContact())
                .role(member.getRole())
                .address(member.getAddress())
                .build();
    }

    public static MembersResponseDto.ReadOneDto toReadOneDto(Members member){
        return MembersResponseDto.ReadOneDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .address(member.getAddress())
                .memberName(member.getMemberName())
                .role(member.getRole())
                .position(member.getPosition())
                .department(member.getDepartment())
                .loveCount(member.getLoveCount())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .onVacation(member.getOnVacation())
                .remainingVacationDays(member.getRemainingVacationDays())
                .build();
    }

    public static MembersResponseDto.UpdateOneDto toUpdateOneDto(Members member){
        return MembersResponseDto.UpdateOneDto.builder()
                .memberId(member.getId())
                .memberName(member.getMemberName())
                .name(member.getName())
                .address(member.getAddress())
                .contact(member.getContact())
                .email(member.getEmail())
                .introduction(member.getIntroduction())
                .profileImageUrl(member.getProfileImageUrl())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }

    public static MembersResponseDto.UpdateOneForAttributeDto toUpdateOneForAttributeDto(
            Members member, String document){
        return MembersResponseDto.UpdateOneForAttributeDto.builder()
                .memberId(member.getId())
                .memberName(member.getMemberName())
                .fileName(document)
                .memberPosition(member.getPosition())
                .memberRole(member.getRole())
                .memberDepartment(member.getDepartment())
                .salary(member.getSalary())
                .build();
    }

    public static MembersResponseDto.UpdateOneForSalaryDto toUpdateOneForSalaryDto(
            Members member){
        return MembersResponseDto.UpdateOneForSalaryDto.builder()
                .memberId(member.getId())
                .memberDepartment(member.getDepartment())
                .memberName(member.getMemberName())
                .memberRole(member.getRole())
                .memberPosition(member.getPosition())
                .changedSalary(member.getSalary())
                .build();
    }

    public static MembersResponseDto.UpdateOneForProfileImageDto toUpdateOneForProfileImageDto(Members member){
        return MembersResponseDto.UpdateOneForProfileImageDto.builder()
                .memberId(member.getId())
                .fromMemberName(member.getMemberName())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    public static MembersResponseDto.DeleteOneForProfileImageDto toDeleteOneForProfileImageDto(Members member){
        return MembersResponseDto.DeleteOneForProfileImageDto.builder()
                .memberId(member.getId())
                .fromMemberName(member.getMemberName())
                .build();
    }

    public static MemberRole toRole(String roleName){
        for(MemberRole role : MemberRole.values()){
            if(role.getAuthority().equalsIgnoreCase(roleName)){
                return role;
            }
        }
        throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_ROLE);
    }

    public static MemberDepartment toDepartment(String departmentName) {
        for (MemberDepartment department : MemberDepartment.values()) {
            if (department.getDepartment().equalsIgnoreCase(departmentName)) {
                return department;
            }
        }
        throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_DEPARTMENT);
    }

    public static MemberPosition toPosition(String positionName){
        for(MemberPosition position : MemberPosition.values()){
            if(position.getPosition().equalsIgnoreCase(positionName)){
                return position;
            }
        }
        throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_POSITION);
    }
}
