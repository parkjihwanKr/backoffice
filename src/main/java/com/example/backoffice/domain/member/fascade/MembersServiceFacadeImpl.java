package com.example.backoffice.domain.member.fascade;

import com.example.backoffice.domain.file.service.FilesService;
import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.MemberRole;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.exception.MembersExceptionEnum;
import com.example.backoffice.domain.member.service.MembersService;
import com.example.backoffice.domain.notification.service.NotificationsService;
import com.example.backoffice.global.exception.CustomException;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.SchedulerCustomException;
import com.example.backoffice.global.scheduler.ScheduledEventType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembersServiceFacadeImpl implements MembersServiceFacade{

    private final FilesService filesService;
    private final MembersService membersService;
    private final NotificationsService notificationsService;
    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void createAdminAccount(){
        if(membersService.existsById(1L)){
            return;
        }
        String rawPassword = "12341234";
        String bcrytPassword = passwordEncoder.encode(rawPassword);
        Members mainAdmin = MembersConverter.toAdminEntity(bcrytPassword);
        membersService.save(mainAdmin);
    }


    // 타당성 검사 추가
    @Override
    @Transactional
    public MembersResponseDto.CreateMembersResponseDto signup(
            MembersRequestDto.CreateMembersRequestDto requestDto){

        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        Members duplicatedInfoMember
                = membersService.findByEmailOrMemberNameOrAddressOrContact(
                requestDto.getEmail(), requestDto.getMemberName(),
                requestDto.getAddress(), requestDto.getContact());
        if(duplicatedInfoMember != null){
            MembersExceptionEnum exceptionType
                    = findExceptionType(requestDto, duplicatedInfoMember);
            switch (exceptionType) {
                case EMAIL
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_EMAIL);
                case ADDRESS
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_ADDRESS);
                case MEMBER_NAME
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_MEMBER_NAME);
                case CONTACT
                        -> throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_CONTACT);
                case NULL
                        -> throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_EXCEPTION_TYPE);
            }
        }

        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());
        Members member = MembersConverter.toEntity(requestDto, bCrytPassword);
        membersService.signup(member);
        return MembersConverter.toCreateDto(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadMemberResponseDto readInfo(
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
        Members updateMember = membersService.save(member);
        return MembersConverter.toUpdateDto(updateMember);
    }

    // 직원의 부서, 직위, 급여 등등 변경
    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberAttributeResponseDto updateAttribute(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateMemberAttributeRequestDto requestDto){
        // 1. 로그인 멤버가 해당 부서, 직위, 급여를 바꿀 수 있는 권한인지? -> Role : Admin이면 바꿀 수 있음
        // 단, 바꾸려는 인물이 직책이 Manager로 승진하는 것이라면 MAIN_ADMIN밖에 권한이 없음

        if(!loginMember.getPosition().equals(MemberPosition.MANAGER)){
            // 해당 조건을 달성하지 못하면
            if(!loginMember.getRole().equals(MemberRole.MAIN_ADMIN)
                    && loginMember.getPosition().equals(MemberPosition.CEO)){
                throw new MembersCustomException(
                        MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
            }
            membersService.findByRoleAndPosition(
                    loginMember.getRole(), loginMember.getPosition());
        } else{
            if(!loginMember.getRole().equals(MemberRole.MAIN_ADMIN)
                    && !loginMember.getRole().equals(MemberRole.ADMIN)){
                throw new MembersCustomException(
                        MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
            }
        }
        // 2. 바꾸려는 인물이 존재하는지?
        Members updateMember = findMember(loginMember, memberId);
        String document
                = filesService.createFileForMemberRole(
                requestDto.getFile(), updateMember);

        updateMember.updateAttribute(
                requestDto.getRole(), requestDto.getDepartment(),
                requestDto.getPosition());

        notificationsService.saveByMemberInfo(
                loginMember.getMemberName(), updateMember.getMemberName(),
                updateMember.getDepartment());

        return MembersConverter.toUpdateAttributeDto(updateMember, document);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberSalaryResponseDto updateSalary(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateMemberSalaryRequestDto requestDto){
        // 1. 로그인 멤버가 바꾸려는 인물과 동일 인물이면 안됨
        // 2. 로그인 멤버가 자기 자신의 급여를 바꿀 순 없음
        Members updateMember
                = membersService.checkMemberId(loginMember.getId(), memberId);

        // 3. 로그인 멤버가 바꿀 권한이 있는지
        // 권한 : 부서가 재정부의 부장이거나 사장인 경우만 가능
        if((loginMember.getDepartment().equals(MemberDepartment.FINANCE) &&
                loginMember.getPosition().equals(MemberPosition.MANAGER))
                || loginMember.getPosition().equals(MemberPosition.CEO)){

            updateMember.updateSalary(requestDto.getSalary());

            notificationsService.saveByMemberInfo(
                    loginMember.getMemberName(), updateMember.getMemberName(),
                    updateMember.getDepartment());

            return MembersConverter.toUpdateSalaryDto(updateMember);
        }else{
            throw new MembersCustomException(
                    MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
        }
    }

    // 프로필 이미지 업로드
    @Override
    @Transactional
    public MembersResponseDto.UpdateMemberProfileImageUrlResponseDto updateProfileImageUrl(
            Long memberId, Members member, MultipartFile image){
        findMember(member, memberId);

        String profileImageUrl = filesService.createImage(image);

        member.updateProfileImage(profileImageUrl);
        return MembersConverter.toUpdateProfileImageDto(member);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public MembersResponseDto.DeleteMemberProfileImageResponseDto deleteProfileImage(
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
        membersService.deleteById(memberId);
    }

    @Override
    public Members findMember(Members member, Long memberId){
        if(!member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_INFO);
        }
        return membersService.findById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findAdmin(
            Long adminId, MemberRole role, MemberDepartment department){
        return membersService.findByIdAndRoleAndDepartment(
                adminId, role, department);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList){
        // 해당 리스트에 대한 member가 있는지 확인
        List<Members> memberList = membersService.findAllById(excludedIdList);
        // 해당 memberList에 저장된 Member와 excludedIdList의 사이즈가 다르면
        // 특정 아이디에 대한 정보가 없다.
        if(memberList.size() != excludedIdList.size()){
            throw new MembersCustomException(MembersExceptionCode.INVALID_MEMBER_IDS);
        }

        List<Members> memberListExcludingDepartmentAndId
                = membersService.findByDepartmentNotInAndIdNotIn(
                excludedDepartmentList, excludedIdList);
        Map<String, MemberDepartment> memberNameMap = new HashMap<>();

        for(Members member : memberListExcludingDepartmentAndId){
            memberNameMap.put(member.getMemberName(), member.getDepartment());
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
        return MembersExceptionEnum.NULL;
    }

    @Override
    @Transactional
    public void updateOnVacationFalse(String memberName){
        Members member = membersService.findByMemberName(memberName);
        member.updateOnVacation(false);
    }

    @Override
    @Transactional
    public void updateOnVacationTrue(String memberName){
        Members member = membersService.findByMemberName(memberName);
        member.updateOnVacation(true);
    }

    @Override
    @Transactional
    public void updateRemainingVacationDays(
            ScheduledEventType scheduledEventType){
        List<Members> memberList = membersService.findAll();
        switch (scheduledEventType) {
            case MONTHLY_UPDATE -> {
                for(Members member : memberList){
                    member.updateRemainingVacation();
                }
            }
            case YEARLY_UPDATE -> {
                for(Members member : memberList){
                    member.updateRemainingVacationYearly();
                }
            }
            default ->
                throw new SchedulerCustomException(GlobalExceptionCode.NOT_FOUND_SCHEDULER_EVENT_TYPE);
        }
    }
}
