package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.image.service.ImagesService;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.repository.MembersRepository;
import com.example.backoffice.global.jwt.dto.TokenDto;
import com.example.backoffice.global.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{

    private final ImagesService imagesService;
    private final AuthenticationService authenticationService;
    private final MembersRepository membersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto){

        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());
        Members member = requestDto.toEntity(bCrytPassword);
        membersRepository.save(member);
        return MembersResponseDto.CreateMembersResponseDto.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public void login(MembersRequestDto.LoginMemberRequestDto requestDto){
        Members loginMember = membersRepository.findByMemberName(requestDto.getMemberName()).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );

        if (!passwordEncoder.matches(requestDto.getPassword(), loginMember.getPassword())) {
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }

        TokenDto token = authenticationService.generateAuthToken(requestDto.getMemberName());
        log.info("Access token : "+token.getAccessToken());
        log.info("Refresh token : "+token.getRefreshToken());
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadMemberResponseDto readMemberInfo(
            Long memberId, Members member){
        Members matchedMember = findMember(member, memberId);
        return MembersResponseDto.ReadMemberResponseDto.from(matchedMember);
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
        member.updateMemberInfo(requestDto, bCrytPassword);
        Members updateMember = membersRepository.save(member);
        return MembersResponseDto.UpdateMemberResponseDto.from(updateMember);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberRoleResponseDto updateMemberRole(
            Long memberId, Members member, MembersRequestDto.UpdateMemberRoleRequestDto requestDto){
        Members updateMember = findMember(member, memberId);
        updateMember.updateRole(requestDto.getRole());
        membersRepository.save(updateMember);
        return MembersResponseDto.UpdateMemberRoleResponseDto.from(member);
    }

    // 프로필 이미지 업로드
    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberProfileImageUrlResponseDto updateMemberProfileImageUrl(
            Long memberId, Members member, MultipartFile image){
        findMember(member, memberId);

        String profileImageUrl = imagesService.uploadFile(image);

        member.updateProfileImage(profileImageUrl);
        membersRepository.save(member);
        return MembersResponseDto.UpdateMemberProfileImageUrlResponseDto.from(member);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public void deleteMemberProfileImageUrl(
            Long memberId, Members member){
        Members existMember = findMember(member, memberId);
        String existMemberProfileImageUrl = existMember.getProfileImageUrl();
        // 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면, true를 리턴
        if(!existMemberProfileImageUrl.isBlank()){
            throw new MembersCustomException(MembersExceptionCode.NOT_BLANK_IMAGE_FILE);
        }
        imagesService.removeFile(existMember.getProfileImageUrl());
        existMember.updateProfileImage(null);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, Members loginMember){
        if(memberId.equals(loginMember.getId())){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
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
        if(!member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
        }
        return member;
    }
}
