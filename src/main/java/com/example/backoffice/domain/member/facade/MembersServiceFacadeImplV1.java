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
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.service.VacationsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MembersServiceFacadeImplV1 implements MembersServiceFacadeV1 {

    private final FilesServiceV1 filesService;
    private final MembersServiceV1 membersService;
    private final NotificationsServiceV1 notificationsService;
    private final VacationsServiceV1 vacationsService;

    // 타당성 검사 추가
    @Override
    @Transactional
    public MembersResponseDto.CreateOneDto createOneForSignup(
            MembersRequestDto.CreateOneDto requestDto){

        membersService.checkPassword(requestDto.getPassword(), requestDto.getPassword());

        membersService.checkDuplicatedMember(requestDto);

        String bCrytPassword
                = membersService.encodePassword(requestDto.getPassword());
        Members member = MembersConverter.toEntity(requestDto, bCrytPassword);
        membersService.signup(member);
        return MembersConverter.toCreateOneDto(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadAvailableMemberNameDto checkAvailableMemberName(
            String requestedMemberName){
        membersService.checkAvailableMemberName(requestedMemberName);

        return MembersConverter.toReadAvailableMemberNameDto(
                true, requestedMemberName);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadOneDetailsDto readOne(
            Long memberId, Members loginMember){
        // 1. 멤버가 자기 자신인 경우
        if(memberId.equals(loginMember.getId())){
            return MembersConverter.toReadOneForDetailsDto(loginMember);
        }
        // 2. 멤버가 자기 자신이 아닌데, 인사 관리를 위해 조회하는 경우
        membersService.findHRManagerOrCEO(loginMember);
        Members foundMember = membersService.findById(memberId);
        return MembersConverter.toReadOneForDetailsDto(foundMember);
    }

    @Override
    @Transactional
    public Page<MembersResponseDto.ReadOneSummaryDto> readByAdmin(
            String department, String position,
            Members loginMember, Pageable pageable) {

        // 1. 로그인 멤버가 HR MANAGER 또는 CEO인지 확인
        membersService.findHRManagerOrCEO(loginMember);

        // 2. 부서와 직위에 따른 필터링 준비
        // ** null -> all과 같은 개념으로 선택되지 않으면 모든 department, position의 값을 부르기 위해
        MemberDepartment memberDepartment =
                department != null ? MembersConverter.toDepartment(department) : null;
        MemberPosition memberPosition =
                position != null ? MembersConverter.toPosition(position) : null;

        // 3. 필터링된 멤버 리스트를 pageable 적용하여 가져오기
        Page<Members> pagedMemberList;

        if (memberDepartment == null && memberPosition == null) {
            // 부서와 직위가 모두 없을 경우, 모든 멤버를 조회
            pagedMemberList = membersService.findAll(pageable);
        } else if (memberDepartment == null) {
            // 직위만 있을 경우, 직위로 필터링된 멤버 조회
            pagedMemberList = membersService.findAllByPosition(pageable, memberPosition);
        } else if (memberPosition == null) {
            // 부서만 있을 경우, 부서로 필터링된 멤버 조회
            pagedMemberList = membersService.findAllByDepartment(pageable, memberDepartment);
        } else {
            // 부서와 직위가 모두 있을 경우, 둘 다로 필터링된 멤버 조회
            pagedMemberList = membersService.findAllByDepartmentAndPosition(memberDepartment, memberPosition, pageable);
        }

        // 4. 페이지화된 멤버 리스트를 DTO로 변환하여 반환
        return MembersConverter.toReadDtoForHrManager(pagedMemberList);
    }


    @Override
    @Transactional
    public MembersResponseDto.UpdateOneDto updateOne(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneDto requestDto){
        // 엔티티가 영속성 컨택스트에 넣어야하기에
        // 수정을 하기 위해선 어떤 엔티티가 변경 되어야 하는지 알아야함
        Members matchedMember = membersService.matchLoginMember(loginMember, memberId);

        // 1. 요청dto의 멤버 네임과 db에 존재하는 memberName이 다르면 안됨
        membersService.matchedMemberName(
                requestDto.getMemberName(), matchedMember.getMemberName());

        // 2. 요청dto의 password와 passwordConfirm이 다르면 안됨
        membersService.checkPassword(
                requestDto.getPassword(), requestDto.getPasswordConfirm());

        // 데이터 베이스 제약 조건까지 넘어가지 않도록 먼저 오류 설정
        membersService.checkIntroductionMaxLength(requestDto.getIntroduction());

        // 3. 요청dto의 contact가 나를 제외한 db에 존재하는 모든 멤버의 연락처와 같으면 안됨
        // 4. 요청dto의 email이 나를 제외한 db에 존재하는 모든 멤버의 이메일과 같으면 안됨
        List<Members> memberListExceptLoginMember
                = membersService.findAllExceptLoginMember(matchedMember.getId());
        for(Members member : memberListExceptLoginMember){
            if(requestDto.getEmail().equals(member.getEmail())){
                throw new MembersCustomException(MembersExceptionCode.DUPLICATED_EMAIL);
            }
            if(requestDto.getContact().equals(member.getContact())){
                throw new MembersCustomException(MembersExceptionCode.DUPLICATED_CONTACT);
            }
        }

        String bCrytPassword = membersService.encodePassword(requestDto.getPassword());

        matchedMember.updateMemberInfo(
                requestDto.getName(), requestDto.getEmail(), requestDto.getAddress(),
                requestDto.getContact(), requestDto.getIntroduction(), bCrytPassword);
        return MembersConverter.toUpdateOneDto(matchedMember);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForAttributeDto updateOneForAttributeByAdmin(
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

        MemberRole role = toRole(requestDto.getRole());
        MemberDepartment department = checkedDepartment(requestDto.getDepartment());
        MemberPosition position = checkedPosition(requestDto.getPosition());

        updateMember.updateAttribute(
                role, department, position);

        String message = loginMember.getMemberName()+"님이 "
                + updateMember.getMemberName()+"님의 직책을 변경하셨습니다.";
        notificationsService.saveForChangeMemberInfo(
                loginMember.getMemberName(), updateMember.getMemberName(),
                updateMember.getDepartment(), message);

        return MembersConverter.toUpdateOneForAttributeDto(updateMember, document);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForSalaryDto updateOneForSalaryByAdmin(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForSalaryDto requestDto){
        // 1. 로그인 멤버가 바꾸려는 인물과 동일 인물이면 안됨
        // 2. 로그인 멤버가 자기 자신의 급여를 바꿀 순 없음
        Members updateMember = null;

        // 2-1. 로그인 멤버가 사장이면 바꿀 수 있음.
        if(!loginMember.getPosition().equals(MemberPosition.CEO)){
            updateMember = membersService.checkDifferentMember(loginMember.getId(), memberId);
        }else{
            updateMember = loginMember;
        }

        // 3. 로그인 멤버가 바꿀 권한이 있는지
        // 권한 : 부서가 재정부의 부장이거나 사장인 경우만 가능
        if((loginMember.getDepartment().equals(MemberDepartment.FINANCE) &&
                loginMember.getPosition().equals(MemberPosition.MANAGER))
                || loginMember.getPosition().equals(MemberPosition.CEO)){

            updateMember.updateSalary(requestDto.getSalary());

            if(!loginMember.getId().equals(updateMember.getId())){
                String message = loginMember.getMemberName() + "님이 "
                        + updateMember.getMemberName() + "님의 급여를 변경하셨습니다.";
                notificationsService.saveForChangeMemberInfo(
                        loginMember.getMemberName(), updateMember.getMemberName(),
                        loginMember.getDepartment(), message);
            }

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
        Members updatedMember
                = membersService.matchLoginMember(loginMember, memberId);

        String profileImageUrl
                = filesService.createMemberProfileImage(image, updatedMember);

        updatedMember.updateProfileImage(profileImageUrl);
        return MembersConverter.toUpdateOneForProfileImageDto(updatedMember);
    }

    // 프로필 이미지 삭제
    @Override
    @Transactional
    public MembersResponseDto.DeleteOneForProfileImageDto deleteOneForProfileImage(
            Long memberId, Members loginMember){
        Members existMember = membersService.matchLoginMember(loginMember, memberId);
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
    public void deleteOneByAdmin(Long memberId, Members loginMember){
        membersService.findHRManagerOrCEO(loginMember);
        membersService.deleteById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public MembersResponseDto.ReadOneForVacationListDto readOneForVacationList(
            Long memberId, Members loginMember){
        membersService.matchLoginMember(loginMember, memberId);

        LocalDateTime today = LocalDateTime.now();
        List<Vacations> memberVacationList
                = vacationsService.findAllByMemberIdAndStartDate(memberId, today);

        return MembersConverter.toReadOneForVacationList(loginMember, memberVacationList);
    }

    @Override
    @Transactional
    public MembersResponseDto.UpdateOneForVacationDto updateMemberVacationByAdmin(
            Long memberId, Members loginMember,
            MembersRequestDto.UpdateOneForVacationDto requestDto){
        Members hrManagerOrCEO = membersService.findHRManagerOrCEO(loginMember);
        Members toMember = membersService.findById(memberId);

        int remainingVacationDays = toMember.getRemainingVacationDays();

        if(requestDto.getVacationDays() - remainingVacationDays > 15){
            throw new MembersCustomException(MembersExceptionCode.VACATION_EXCEEDS_LIMIT);
        }else if((requestDto.getVacationDays() - remainingVacationDays >= 0) ||
                (requestDto.getVacationDays() - remainingVacationDays <= 15)){
            toMember.updateRemainingVacationDays(requestDto.getVacationDays());
        }else if(requestDto.getVacationDays() < 0) {
            throw new MembersCustomException(MembersExceptionCode.VACATION_UNDER_ZERO);
        }

        if(!hrManagerOrCEO.getId().equals(toMember.getId())){
            String message = hrManagerOrCEO.getMemberName() + "님이 "
                    + toMember.getMemberName() + "님의 휴가 정보를 변경하셨습니다.";
            notificationsService.saveForChangeMemberInfo(
                    hrManagerOrCEO.getMemberName(), toMember.getMemberName(),
                    toMember.getDepartment(), message);
        }

        return MembersConverter.toUpdateOneForVacationDto(
                toMember.getId(), toMember.getMemberName(), toMember.getRemainingVacationDays());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembersResponseDto.ReadNameDto> readNameList(Members loginMember) {
        membersService.hasAdminAccess(loginMember.getRole());
        List<Members> memberList = membersService.findAll();
        return MembersConverter.toReadNameListDto(memberList);
    }

    public MemberRole toRole(String role){
        return MembersConverter.toRole(role);
    }

    public MemberDepartment checkedDepartment(String department){
        return MembersConverter.toDepartment(department);
    }

    public MemberPosition checkedPosition(String position){
        return MembersConverter.toPosition(position);
    }
}
