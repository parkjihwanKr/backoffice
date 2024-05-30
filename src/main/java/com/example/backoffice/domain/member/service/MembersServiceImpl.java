package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.file.service.FilesService;
import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.exception.MembersExceptionEnum;
import com.example.backoffice.domain.member.repository.MembersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{

    private final FilesService filesService;
    private final MembersRepository membersRepository;
    private final PasswordEncoder passwordEncoder;

    // 관리자 설정
    @PostConstruct
    public void createAdminAccount(){
        if(membersRepository.existsById(1L)){
            return;
        }
        String rawPassword = "12341234";
        String bcrytPassword = passwordEncoder.encode(rawPassword);
        Members mainAdmin = MembersConverter.toAdminEntity(bcrytPassword);
        membersRepository.save(mainAdmin);
    }

    // 타당성 검사 추가
    @Override
    @Transactional
    public MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto){

        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        Members duplicateInfoMember
                = membersRepository.findByEmailOrMemberNameOrAddressOrContact(
                        requestDto.getEmail(), requestDto.getMemberName(),
                requestDto.getAddress(), requestDto.getContact()).orElse(null);
        if(duplicateInfoMember != null){
            MembersExceptionEnum exceptionType = findExceptionType(requestDto, duplicateInfoMember);
            switch (exceptionType) {
                case EMAIL
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_EMAIL);
                case ADDRESS
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_ADDRESS);
                case MEMBER_NAME
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_MEMBER_NAME);
                case CONTACT
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_CONTACT);
                default
                        -> log.error("Not Found Exception Error : " + exceptionType);
            }
        }

        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());
        Members member = MembersConverter.toEntity(requestDto, bCrytPassword);
        membersRepository.save(member);
        return MembersConverter.toCreateDto(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadMemberResponseDto readMemberInfo(
            Long memberId, Members member){
        Members matchedMember = findMember(member, memberId);
        return MembersConverter.toReadDto(matchedMember);
    }


    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberResponseDto updateMember(
            Long memberId, Members member, MembersRequestDto.UpdateMemberRequestDto requestDto){
        if(!requestDto.getMemberName().equals(member.getMemberName())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_MEMBER_NAME);
        }
        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());
        member.updateMemberInfo(
                requestDto.getName(), requestDto.getEmail(), requestDto.getAddress(),
                requestDto.getContact(), requestDto.getIntroduction(), bCrytPassword);
        Members updateMember = membersRepository.save(member);
        return MembersConverter.toUpdateDto(updateMember);
    }

    // 권한 변경
    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberRoleResponseDto updateMemberRole(
            Long memberId, Members member,
            MultipartFile file){
        Members updateMember = findMember(member, memberId);
        String document = filesService.createFileForMemberRole(file, updateMember);
        membersRepository.save(updateMember);
        return MembersConverter.toUpdateRoleDto(member, document);
    }

    // 프로필 이미지 업로드
    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberProfileImageUrlResponseDto updateMemberProfileImageUrl(
            Long memberId, Members member, MultipartFile image){
        findMember(member, memberId);

        String profileImageUrl = filesService.createImage(image);

        member.updateProfileImage(profileImageUrl);
        membersRepository.save(member);
        return MembersConverter.toUpdateProfileImageDto(member);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public MembersResponseDto.DeleteMemberProfileImageResponseDto deleteMemberProfileImage(
            Long memberId, Members member){
        Members existMember = findMember(member, memberId);
        String existMemberProfileImageUrl = existMember.getProfileImageUrl();
        // 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면, true를 리턴
        if(existMemberProfileImageUrl.isBlank()){
            throw new MembersCustomException(MembersExceptionCode.NOT_BLANK_IMAGE_FILE);
        }
        filesService.deleteImage(existMember.getProfileImageUrl());
        existMember.updateProfileImage(null);

        return MembersConverter.toDeleteProfileImageDto(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, Members loginMember){
        findMember(loginMember, memberId);
        membersRepository.deleteById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findById(Long memberId){
        return membersRepository.findById(memberId).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    @Override
    public Members findMember(Members member, Long memberId){
        if(!member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
        }
        return member;
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
    public Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList){
        // 해당 리스트에 대한 member가 있는지 확인
        List<Members> memberList = membersRepository.findAllById(excludedIdList);
        // 해당 memberList에 저장된 Member와 excludedIdList의 사이즈가 다르면
        // 특정 아이디에 대한 정보가 없다.
        if(memberList.size() != excludedIdList.size()){
            throw new MembersCustomException(MembersExceptionCode.INVALID_MEMBER_IDS);
        }

        List<Members> memberListExcludingDepartmentAndId
                = membersRepository.findByMemberDepartmentNotInAndIdNotIn(
                        excludedDepartmentList, excludedIdList);
        Map<String, MemberDepartment> memberNameMap = new HashMap<>();

        for(Members member : memberListExcludingDepartmentAndId){
            memberNameMap.put(member.getMemberName(), member.getMemberDepartment());
        }
        return memberNameMap;
    }

    private MembersExceptionEnum findExceptionType(
            MembersRequestDto.CreateMembersRequestDto requestDto, Members duplicatedInfoMember){
        if(requestDto.getContact().equals(duplicatedInfoMember.getContact())){
            return MembersExceptionEnum.CONTACT;
        }
        if(requestDto.getEmail().equals(duplicatedInfoMember.getEmail())){
            return MembersExceptionEnum.EMAIL;
        }
        if(requestDto.getAddress().equals(duplicatedInfoMember.getAddress())){
            return MembersExceptionEnum.ADDRESS;
        }
        if(requestDto.getMemberName().equals(duplicatedInfoMember.getMemberName())){
            return MembersExceptionEnum.MEMBER_NAME;
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Members findAdmin(
            Long adminId, MemberRole role, MemberDepartment department){
        return membersRepository.findByIdAndRoleAndMemberDepartment(adminId, role, department)
                .orElseThrow(
                        ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
                );
    }
}
