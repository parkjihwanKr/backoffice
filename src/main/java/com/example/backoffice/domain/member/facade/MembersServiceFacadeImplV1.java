package com.example.backoffice.domain.member.facade;

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
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.service.NotificationsService;
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
public class MembersServiceFacadeImplV1 implements MembersServiceFacadeV1 {

    private final FilesService filesService;
    private final MembersServiceV1 membersService;
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
    public MembersResponseDto.CreateOneDto signup(
            MembersRequestDto.CreateOneDto requestDto){

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
        return MembersConverter.toCreateOneDto(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadOneDto readOne(
            Long memberId, Members member){
        Members matchedMember = findMember(member, memberId);
        return MembersConverter.toReadOneDto(matchedMember);
    }


    @Override
    @Transactional
    public MembersResponseDto.UpdateOneDto updateOne(
            Long memberId, Members member, MultipartFile multipartFile,
            MembersRequestDto.UpdateOneDto requestDto){
        if(!requestDto.getMemberName().equals(member.getMemberName())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_MEMBER_NAME);
        }
        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());

        String profileImageUrl = filesService.createImage(multipartFile);
        member.updateMemberInfo(
                requestDto.getName(), requestDto.getEmail(), requestDto.getAddress(),
                requestDto.getContact(), requestDto.getIntroduction(),
                bCrytPassword, profileImageUrl);
        Members updateMember = membersService.save(member);
        return MembersConverter.toUpdateOneDto(updateMember);
    }

    // 직원의 부서, 직위, 급여 등등 변경
    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForAttributeDto updateOneForAttribute(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForAttributeDto requestDto,
            MultipartFile multipartFile){
        Members updateMember
                = membersService.checkMemberId(loginMember.getId(), memberId);
        boolean isHRManager = loginMember.getPosition().equals(MemberPosition.MANAGER)
                && loginMember.getDepartment().equals(MemberDepartment.HR);
        boolean isMainAdmin = loginMember.getPosition().equals(MemberPosition.CEO);

        if(isHRManager){
            // 메인 어드민의 속성을 변경하려는 경우 예외
            if (requestDto.getPosition().equals(MemberPosition.CEO)
                    && requestDto.getDepartment().equals(MemberDepartment.HR)) {
                throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
            }
            // 각 부서장(Manager)의 직책을 변경하려는 경우 예외
            if (updateMember.getPosition().equals(MemberPosition.MANAGER)) {
                throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
            }
            // 모든 부서의 차장을 부장으로 승진시키려는 경우 예외
            if (requestDto.getPosition().equals(MemberPosition.MANAGER)
                    && updateMember.getPosition().equals(MemberPosition.ASSISTANT_MANAGER)) {
                throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
            }
        }

        // 로그인 멤버가 메인 어드민이 아닌 경우 예외
        if (!isMainAdmin && !isHRManager) {
            throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
        }

        String document
                = filesService.createFileForMemberRole(multipartFile, updateMember);

        MemberRole role = checkedRole(requestDto.getRole());
        MemberDepartment department = checkedDepartment(requestDto.getDepartment());
        MemberPosition position = checkedPosition(requestDto.getPosition());

        updateMember.updateAttribute(
                role, department, position, requestDto.getSalary());

        notificationsService.saveByMemberInfo(
                loginMember.getMemberName(), updateMember.getMemberName(),
                updateMember.getDepartment());

        return MembersConverter.toUpdateOneForAttributeDto(updateMember, document);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForSalaryDto updateOneForSalary(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForSalaryDto requestDto){
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

            return MembersConverter.toUpdateOneForSalaryDto(updateMember);
        }else{
            throw new MembersCustomException(
                    MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
        }
    }

    // 프로필 이미지 업로드
    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForProfileImageDto updateOneForProfileImage(
            Long memberId, Members member, MultipartFile image){
        findMember(member, memberId);

        String profileImageUrl = filesService.createImage(image);

        member.updateProfileImage(profileImageUrl);
        return MembersConverter.toUpdateOneForProfileImageDto(member);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public MembersResponseDto.DeleteOneForProfileImageDto deleteOneForProfileImage(
            Long memberId, Members member){
        Members existMember = findMember(member, memberId);
        String existMemberProfileImageUrl = existMember.getProfileImageUrl();
        // 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면, true를 리턴
        if(existMemberProfileImageUrl.isBlank()){
            throw new MembersCustomException(MembersExceptionCode.NOT_BLANK_IMAGE_FILE);
        }
        filesService.deleteImage(existMember.getProfileImageUrl());
        existMember.updateProfileImage(null);

        return MembersConverter.toDeleteOneForProfileImageDto(member);
    }

    @Override
    @Transactional
    public void deleteOne(Long memberId, Members loginMember){
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
        List<Members> memberList = membersService.findAllById(excludedIdList);
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
            MembersRequestDto.CreateOneDto requestDto, Members duplicatedInfoMember){
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
    public void updateOneForOnVacationFalse(String memberName){
        Members member = membersService.findByMemberName(memberName);
        member.updateOnVacation(false);
    }

    @Override
    @Transactional
    public void updateOneForOnVacationTrue(String memberName){
        Members member = membersService.findByMemberName(memberName);
        member.updateOnVacation(true);
    }

    @Override
    @Transactional
    public void updateOneForRemainingVacationDays(
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

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByDepartment(String department){
        MemberDepartment memberDepartment = MembersConverter.toDepartment(department);
        return membersService.findAllByDepartment(memberDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByPosition(String position){
        MemberPosition memberPosition = MembersConverter.toPosition(position);
        return membersService.findAllByPosition(memberPosition);
    }

    public MemberRole checkedRole(String role){
        return MembersConverter.toRole(role);
    }

    public MemberDepartment checkedDepartment(String department){
        return MembersConverter.toDepartment(department);
    }

    public MemberPosition checkedPosition(String position){
        return MembersConverter.toPosition(position);
    }
}
