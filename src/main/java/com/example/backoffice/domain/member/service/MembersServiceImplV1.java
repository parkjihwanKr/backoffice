package com.example.backoffice.domain.member.service;

import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.dto.MembersRequestDto;
import com.example.backoffice.domain.member.dto.MembersResponseDto;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.exception.MembersCustomException;
import com.example.backoffice.domain.member.exception.MembersExceptionCode;
import com.example.backoffice.domain.member.exception.MembersExceptionEnum;
import com.example.backoffice.domain.member.repository.MembersRepository;
import com.example.backoffice.global.exception.GlobalExceptionCode;
import com.example.backoffice.global.exception.SchedulerCustomException;
import com.example.backoffice.global.scheduler.ScheduledEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MembersServiceImplV1 implements MembersServiceV1 {

    private final PasswordEncoder passwordEncoder;
    private final MembersRepository membersRepository;

    @Override
    @Transactional
    public void signup(Members member){
        membersRepository.save(member);
    }

    @Override
    @Transactional
    public void checkAvailableMemberName(String requestedMemberName){
        if (!requestedMemberName.matches("^[a-z0-9]{8,16}$")) {
            throw new MembersCustomException(MembersExceptionCode.INVALID_MEMBER_NAME);
        }

        if(isExistMemberName(requestedMemberName)){
            throw new MembersCustomException(
                    MembersExceptionCode.EXISTS_MEMBER);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Members findById(Long memberId){
        return membersRepository.findById(memberId).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Members checkDifferentMember(Long fromMemberId, Long toMemberId){
        if(toMemberId.equals(fromMemberId)){
            throw new MembersCustomException(MembersExceptionCode.MATCHED_LOGIN_MEMBER);
        }
        return findById(toMemberId);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkDuplicatedMember(MembersRequestDto.CreateOneDto requestDto){
        Members duplicatedInfoMember
                = membersRepository.findByEmailOrMemberNameOrAddressOrContact(
                        requestDto.getEmail(), requestDto.getMemberName(),
                requestDto.getAddress(), requestDto.getContact()).orElse(null);
        if(duplicatedInfoMember != null){
            MembersExceptionEnum exceptionType
                    = findExceptionType(requestDto, duplicatedInfoMember);
            switch (exceptionType) {
                case EMAIL
                        -> throw new MembersCustomException(
                        MembersExceptionCode.DUPLICATED_EMAIL);
                case ADDRESS
                        -> throw new MembersCustomException(MembersExceptionCode.DUPLICATED_ADDRESS);
                case MEMBER_NAME
                        -> throw new MembersCustomException(MembersExceptionCode.DUPLICATED_MEMBER_NAME);
                case CONTACT
                        -> throw new MembersCustomException(MembersExceptionCode.DUPLICATED_CONTACT);
                case NULL
                        -> throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_EXCEPTION_TYPE);
            }
        }
    }

    @Override
    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(Long memberId){
        return membersRepository.existsById(memberId);
    }

    @Override
    @Transactional
    public Members save(Members member){
        return membersRepository.save(member);
    }

    @Override
    @Transactional
    public void deleteById(Long memberId){
        membersRepository.deleteById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllById(List<Long> memberIdList){
        return membersRepository.findAllById(memberIdList);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findHRManager(){
        return findByPositionAndDepartment(MemberPosition.MANAGER, MemberDepartment.HR);
    }

    @Override
    @Transactional(readOnly = true)
    public Long findMemberTotalCount(){
        return membersRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByMemberName(String memberName){
        return membersRepository.findByMemberName(memberName).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAll(){
        return membersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByDepartment(MemberDepartment department){
        List<Members> memberList = membersRepository.findAllByDepartment(department);
        if (memberList.isEmpty()){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
        }
        return memberList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllExceptLoginMember(Long exceptMemberId){
        return membersRepository.findAllByIdNotIn(Collections.singletonList(exceptMemberId));
    }

    @Override
    public void addVacationDays(Members onVacationMember, int plusVacationDays){
        onVacationMember.plusRemainingVacation(plusVacationDays);
    }

    @Override
    public void minusVacationDays(Members onVacationMember, int minusVacationDays){
        onVacationMember.minusRemainingVacation(minusVacationDays);
    }

    @Override
    public Members findHRManagerOrCEO(Members member){
        if ((member.getDepartment().equals(MemberDepartment.HR)
                && member.getPosition().equals(MemberPosition.MANAGER))){
            return member;
        }
        if(member.getPosition().equals(MemberPosition.CEO)){
            return member;
        }
        throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
    }

    @Override
    public MemberDepartment findDepartment(String department){
        return MembersConverter.toDepartment(department);
    }


    @Override
    @Cacheable(
            value = "membersByDeptAndPos",
            cacheManager = "cacheManagerForCachedMember",
            key = "#department + '-' + #position")
    public Page<Members> findAllByDepartmentAndPosition(
            MemberDepartment department, MemberPosition position,
            Pageable pageable){
        return membersRepository.findAllByDepartmentAndPosition(department, position, pageable);
    }

    @Override
    public Page<Members> findAll(Pageable pageable){
        return membersRepository.findAll(pageable);
    }

    @Override
    public Page<Members> findAllByDepartment(Pageable pageable, MemberDepartment department){
        return membersRepository.findAllByDepartment(pageable, department);
    }

    @Override
    public Page<Members> findAllByPosition(Pageable pageable, MemberPosition position){
        return membersRepository.findAllByPosition(pageable, position);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findAuditManagerOrCeo(Long memberId){
        Members foundMember = findById(memberId);
        if(foundMember.getPosition().equals(MemberPosition.CEO)
                || (foundMember.getPosition().equals(MemberPosition.MANAGER)
                && foundMember.getDepartment().equals(MemberDepartment.AUDIT))) {
            return foundMember;
        }
        throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
    }

    @Override
    @Transactional(readOnly = true)
    public Members matchLoginMember(Members member, Long memberId){
        if(!member.getId().equals(memberId)){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_INFO);
        }
        return findById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, MemberDepartment> findMemberNameListExcludingDepartmentListAndIdList(
            List<MemberDepartment> excludedDepartmentList,
            List<Long> excludedIdList){
        List<Members> memberList = findAllById(excludedIdList);

        if(memberList.size() != excludedIdList.size()){
            throw new MembersCustomException(MembersExceptionCode.INVALID_MEMBER_IDS);
        }

        List<Members> memberListExcludingDepartmentAndId = null;

        if (excludedDepartmentList.isEmpty() && excludedIdList.isEmpty()) {
            memberListExcludingDepartmentAndId = membersRepository.findAll();
        } else if (excludedIdList.isEmpty()) {
            memberListExcludingDepartmentAndId
                    = membersRepository.findByDepartmentNotIn(
                            excludedDepartmentList);
        } else if (excludedDepartmentList.isEmpty()) {
            memberListExcludingDepartmentAndId
                    = membersRepository.findByIdNotIn(excludedIdList);
        } else {
            memberListExcludingDepartmentAndId
                    = membersRepository.findByDepartmentNotInAndIdNotIn(
                            excludedDepartmentList, excludedIdList);
        }

        Map<String, MemberDepartment> memberNameMap = new HashMap<>();

        for(Members member : memberListExcludingDepartmentAndId){
            memberNameMap.put(member.getMemberName(), member.getDepartment());
        }
        return memberNameMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Members findCeoByMemberName(String memberName){
        Members ceo = membersRepository.findByMemberName(memberName).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.DUPLICATED_MEMBER_NAME));
        if(!ceo.getPosition().equals(MemberPosition.CEO)){
            throw new MembersCustomException(MembersExceptionCode.RESTRICTED_ACCESS_MEMBER);
        }
        return ceo;
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByPosition(MemberPosition position) {
        return membersRepository.findByPosition(position).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER));
    }

    @Override
    @Transactional
    public void updateOneForRemainingVacationDays(
            ScheduledEventType scheduledEventType){
        List<Members> memberList = findAll();
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
    private Members findByPositionAndDepartment(
            MemberPosition position, MemberDepartment department){
        return membersRepository.findByPositionAndDepartment(
                position, department).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER));
    }

    @Override
    @Transactional
    public void updateOneForOnVacationFalse(Long onVacationMemberId){
        Members member = findById(onVacationMemberId);
        member.updateOnVacation(false);
    }

    @Override
    @Transactional
    public void updateOneForOnVacationTrue(Long onVacationMemberId){
        Members member = findById(onVacationMemberId);
        member.updateOnVacation(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByMemberName(String memberName) {
        List<Members> memberList = membersRepository.findAllByMemberName(memberName);
        if(memberList.isEmpty()){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
        }
        return memberList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Members> findAllByDepartment(MemberDepartment department, String memberName){
        List<Members> memberList = membersRepository.findAllByDepartmentAndMemberName(
                department, memberName);
        if(memberList.isEmpty()){
            throw new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER);
        }
        return memberList;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isManagerOrCeo(Members loginMember){
        if(!loginMember.getPosition().equals(MemberPosition.MANAGER)){
            if(!loginMember.getPosition().equals(MemberPosition.CEO)){
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Members findByIdAndDepartment(Long memberId, MemberDepartment department){
        return membersRepository.findByIdAndDepartment(memberId, department).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER));
    }

    @Override
    @Transactional(readOnly = true)
    public Members findItManager() {
        return membersRepository.findByPositionAndDepartment(
                        MemberPosition.MANAGER, MemberDepartment.IT).orElseGet(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Members findCeo() {
        return membersRepository.findByPosition(MemberPosition.CEO).orElseThrow(
                ()-> new MembersCustomException(MembersExceptionCode.NOT_FOUND_MEMBER));
    }

    @Override
    @Transactional
    public void checkPassword(String password, String passwordConfirm){
        if(!password.equals(passwordConfirm)){
            throw new MembersCustomException(
                    MembersExceptionCode.NOT_MATCHED_PASSWORD_AND_PASSWORD_CONFIRM);
        }
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
    @Transactional(readOnly = true)
    public Boolean isExistMemberName(String memberName){
        return membersRepository.existsByMemberName(memberName);
    }

    @Override
    public void checkIntroductionMaxLength(String requestedIntroduction){
        if(requestedIntroduction.length() > 500){
            throw new MembersCustomException(MembersExceptionCode.MAX_LENGTH_500);
        }
    }

    @Override
    public void matchedMemberName(String memberName, String loginMemberName){
        if(!memberName.equals(loginMemberName)){
            throw new MembersCustomException(MembersExceptionCode.NOT_MATCHED_MEMBER_NAME);
        }
    }
}
