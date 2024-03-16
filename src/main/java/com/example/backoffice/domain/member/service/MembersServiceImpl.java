package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{

    private final MembersRepository membersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto){

        if(requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());
        Members member = MembersRequestDto.CreateMembersRequestDto.from(requestDto, bCrytPassword);
        membersRepository.save(member);
        return MembersResponseDto.CreateMembersResponseDto.of(member);
    }

    @Override
    @Transactional(readOnly = true)
    public void login(MembersRequestDto.LoginMemberRequestDto requestDto){
        Members loginMember = membersRepository.findByMemberName(requestDto.getMemberName());
        if(loginMember == null){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
        }
        if(loginMember.getPassword().equals(requestDto.getPassword())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadMemberResponseDto readMemberInfo(
            Long memberId, Members member){
        Members matchedMember = findMember(member, memberId);
        return MembersResponseDto.ReadMemberResponseDto.of(matchedMember);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberResponseDto updateMember(
            Long memberId, Members member, MembersRequestDto.UpdateMemberRequestDto requestDto){
        if(requestDto.getPassword().equals(requestDto.getPasswordCofirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());
        member.updateMemberInfo(requestDto, bCrytPassword);
        Members updateMember = membersRepository.save(member);
        return MembersResponseDto.UpdateMemberResponseDto.of(updateMember);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberRoleResponseDto updateMemberRole(
            Long memberId, Members member, MembersRequestDto.UpdateMemberRoleRequestDto requestDto){
        Members updateMember = findMember(member, memberId);
        updateMember.updateRole(requestDto.getRole());
        membersRepository.save(updateMember);
        return MembersResponseDto.UpdateMemberRoleResponseDto.of(member);
    }

    // 프로필 이미지 업로드
    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberProfileImageUrlResponseDto updateMemberProfileImageUrl(
            Long memberId, Members member, MultipartFile image){
        findMember(member, memberId);
        if (Objects.requireNonNull(image.getOriginalFilename()).isBlank()) {
            throw new MembersCustomException(MembersExceptionCode.NOT_BLANK_IMAGE_FILE);
        }
        List<String> extension = List.of("jpg","jpeg","png");

        // 확장자 구분
        String[] fileNameArray = image.getOriginalFilename().split(".");
        for(int i = 0; i<extension.size(); i++){
            if(fileNameArray[1].equals(extension.get(i))){
                break;
            }else if(!fileNameArray[1].equals(extension.get(i)) && i == extension.size()-1){
                throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_IMAGE_FILE);
            }
        }

        // fix #3 IOExcpetion에 대한 예외 처리 필요
        UUID uuid = UUID.randomUUID();
        String originalFilename = image.getOriginalFilename();
        String uuidProfileImageUrl = uuid+"_"+originalFilename;
        member.updateProfileImage(uuidProfileImageUrl);

        membersRepository.save(member);
        return MembersResponseDto.UpdateMemberProfileImageUrlResponseDto.of(member);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public void deleteMemberProfileImageUrl(
            Long memberId, Members member){
        findMember(member, memberId);
        membersRepository.deleteProfileImageUrl(memberId);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, Members loginMember){
        if(memberId.equals(loginMember.getId())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_MEMBER);
        }
        membersRepository.deleteById(memberId);
    }

    // 해당 MemberId 찾기
    @Transactional(readOnly = true)
    public Members findById(Long memberId){
        return membersRepository.findById(memberId).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    private Members findMember(Members member, Long memberId){
        if(member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_MEMBER);
        }
        return member;
    }
}
