package com.example.backoffice.domain.member.facade;

import com.example.backoffice.domain.file.service.FilesServiceV1;
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
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembersServiceFacadeImplV1 implements MembersServiceFacadeV1 {

    private final FilesServiceV1 filesService;
    private final MembersServiceV1 membersService;
    private final NotificationsServiceV1 notificationsService;
    private final PasswordEncoder passwordEncoder;

    // 해당 부분 지워야함
    @PostConstruct
    public void createOneForAdmin(){
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
    public MembersResponseDto.CreateOneDto createOneForSignup(
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
            Long memberId, Members loginMember){
        Members matchedMember = matchLoginMember(loginMember, memberId);
        return MembersConverter.toReadOneDto(matchedMember);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateOneDto updateOne(
            Long memberId, Members loginMember, MultipartFile multipartFile,
            MembersRequestDto.UpdateOneDto requestDto){
        // 엔티티가 영속성 컨택스트에 넣어야하기에
        // 수정을 하기 위해선 어떤 엔티티가 변경 되어야 하는지 알아야함
        Members matchedMember = matchLoginMember(loginMember, memberId);

        // 1. 요청dto의 멤버 네임과 db에 존재하는 memberName이 다르면 안됨
        if(!requestDto.getMemberName().equals(matchedMember.getMemberName())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_MEMBER_NAME);
        }
        // 2. 요청dto의 password와 passwordConfirm이 다르면 안됨
        if(!requestDto.getPassword().equals(requestDto.getPasswordConfirm())){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_PASSWORD);
        }
        // 3. 요청dto의 contact가 나를 제외한 db에 존재하는 모든 멤버의 연락처와 같으면 안됨
        // 4. 요청dto의 email이 나를 제외한 db에 존재하는 모든 멤버의 이메일과 같으면 안됨
        List<Members> memberListExceptLoginMember
                = membersService.findAllExceptLoginMember(matchedMember.getId());
        for(Members member : memberListExceptLoginMember){
            if(requestDto.getEmail().equals(member.getEmail())){
                throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_EMAIL);
            }
            if(requestDto.getContact().equals(member.getContact())){
                throw new MembersCustomException(MembersExceptionCode.MATCHED_MEMBER_INFO_CONTACT);
            }
        }

        String bCrytPassword = passwordEncoder.encode(requestDto.getPassword());

        String profileImageUrl = filesService.createImage(multipartFile);
        matchedMember.updateMemberInfo(
                requestDto.getName(), requestDto.getEmail(), requestDto.getAddress(),
                requestDto.getContact(), requestDto.getIntroduction(),
                bCrytPassword, profileImageUrl);
        return MembersConverter.toUpdateOneDto(matchedMember);
    }

    // 직원의 부서, 직위, 급여 등등 변경
    // 해당 변경 사항은 메인 어드민이거나 인사부장 제외하고는 인사 발령을 건드릴 수 없다.
    // 인사 부장은 메인 어드민, 각 부서의 부장의 직위를 변경할 수 없다.
    // 즉, 인사 부장은 메인 어드민을 제외한 각 부서의 직원들의 직책은 변경할 수 있다.
    // ex) IT MANAGER -> SALES MANAGER
    // 메인 어드민은 모든 직원의 직책, 직위를 변경할 수 있다.
    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForAttributeDto updateOneForAttribute(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForAttributeDto requestDto,
            MultipartFile multipartFile) throws MembersCustomException {
        Members updateMember
                = membersService.checkDifferentMember(loginMember.getId(), memberId);
        boolean isHRManager = loginMember.getPosition().equals(MemberPosition.MANAGER)
                && loginMember.getDepartment().equals(MemberDepartment.HR);
        boolean isMainAdmin = loginMember.getPosition().equals(MemberPosition.CEO);

        // 메인 어드민이 아닌 경우에 대한 처리
        if (!isMainAdmin) {
            // HR Manager일 경우의 권한 제한
            if (isHRManager) {
                // 메인 어드민 또는 부장을 변경하려는 시도는 금지됨
                if (requestDto.getPosition().equals(MemberPosition.CEO.getPosition())
                        || updateMember.getPosition().equals(MemberPosition.MANAGER)) {
                    throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
                }

                // 부장 직위를 부여하려는 시도는 금지됨
                if (requestDto.getPosition().equals(MemberPosition.MANAGER.getPosition())) {
                    throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
                }
            } else {
                // 메인 어드민도 아니고 HR Manager도 아닌 경우
                throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
            }
        }

        String document
                = filesService.createOneForMemberRole(multipartFile, updateMember);

        MemberRole role = checkedRole(requestDto.getRole());
        MemberDepartment department = checkedDepartment(requestDto.getDepartment());
        MemberPosition position = checkedPosition(requestDto.getPosition());

        updateMember.updateAttribute(
                role, department, position);

        notificationsService.saveForChangeMemberInfo(
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
                = membersService.checkDifferentMember(loginMember.getId(), memberId);

        // 3. 로그인 멤버가 바꿀 권한이 있는지
        // 권한 : 부서가 재정부의 부장이거나 사장인 경우만 가능
        if((loginMember.getDepartment().equals(MemberDepartment.FINANCE) &&
                loginMember.getPosition().equals(MemberPosition.MANAGER))
                || loginMember.getPosition().equals(MemberPosition.CEO)){

            updateMember.updateSalary(requestDto.getSalary());

            notificationsService.saveForChangeMemberInfo(
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
            Long memberId, Members loginMember, MultipartFile image){
        matchLoginMember(loginMember, memberId);

        String profileImageUrl = filesService.createImage(image);

        loginMember.updateProfileImage(profileImageUrl);
        return MembersConverter.toUpdateOneForProfileImageDto(loginMember);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public MembersResponseDto.DeleteOneForProfileImageDto deleteOneForProfileImage(
            Long memberId, Members loginMember){
        Members existMember = matchLoginMember(loginMember, memberId);
        String existMemberProfileImageUrl = existMember.getProfileImageUrl();
        // 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면, true를 리턴
        if(existMemberProfileImageUrl.isBlank()){
            throw new MembersCustomException(MembersExceptionCode.NOT_BLANK_IMAGE_FILE);
        }
        filesService.deleteImage(existMember.getProfileImageUrl());
        existMember.updateProfileImage(null);

        return MembersConverter.toDeleteOneForProfileImageDto(loginMember);
    }

    @Override
    @Transactional
    public void deleteOne(Long memberId, Members loginMember){
        matchLoginMember(loginMember, memberId);
        membersService.deleteById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Members matchLoginMember(Members member, Long memberId){
        if(!member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_INFO);
        }
        return membersService.findById(memberId);
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

    @Override
    @Transactional(readOnly = true)
    public Members findHRManager(){
        return membersService.findHRManager();
    }

    @Override
    @Transactional
    public void updateOneForOnVacationFalse(Long onVacationMemberId){
        Members member = membersService.findById(onVacationMemberId);
        member.updateOnVacation(false);
    }

    @Override
    @Transactional
    public void updateOneForOnVacationTrue(Long onVacationMemberId){
        Members member = membersService.findById(onVacationMemberId);
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

    @Override
    @Transactional(readOnly = true)
    public Members findByMemberName(String memberName){
        return membersService.findByMemberName(memberName);
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
