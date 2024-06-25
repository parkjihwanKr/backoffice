package com.example.backoffice.domain.evaluation.service;

import com.example.backoffice.domain.evaluation.converter.EvaluationsConverter;
import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;
import com.example.backoffice.domain.evaluation.repository.EvaluationsRepository;
import com.example.backoffice.domain.member.converter.MembersConverter;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationsServiceV1Impl implements EvaluationsServiceV1{

    private final MembersServiceV1 membersService;
    private final NotificationsServiceFacade notificationsServiceFacade;
    private final EvaluationsRepository evaluationsRepository;

    @Override
    @Transactional
    public EvaluationsResponseDto.CreateOneForDepartmentDto createOneForDepartment(
            Members loginMember, EvaluationsRequestDto.CreateOneForDepartmentDto requestDto){

        // 1. 멤버 확인
        membersService.findById(loginMember.getId());
        // 2. 적절한 부서에서 설문 조사를 만들었는지?
        MemberDepartment department = MembersConverter.toDepartment(requestDto.getDepartment());
        if(!loginMember.getDepartment().equals(department)
                && (loginMember.getPosition().equals(MemberPosition.MANAGER)
                || loginMember.getPosition().equals(MemberPosition.CEO))){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_DEPARTMENT_ACCESS);
        }
        // 3. 요청한 날짜가 시작, 마감 날짜가 분기에 따라 잘 나뉘었는지?
        Integer quarter = validateAndDetermineQuarter(requestDto.getStartDate(), requestDto.getEndDate());

        int year = requestDto.getStartDate().getYear();
        String title = year+"년 "+quarter+"분기 설문조사";

        List<Members> memberList = membersService.findAllByDepartment(department);

        Evaluations evaluations
                = EvaluationsConverter.toEntity(
                        title, year, quarter, requestDto.getDescription(),
                loginMember.getDepartment(), memberList);
        evaluationsRepository.save(evaluations);

        for(Members member : memberList){
            notificationsServiceFacade.createNotification(
                    NotificationsConverter.toNotificationData(
                            loginMember, member, null, null,
                            null, null, title),
                    NotificationType.EVALUATION);
        }

        return EvaluationsConverter.toCreateOneForDepartmentDto(evaluations);
    }


    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneForDepartmentDto readOneForDepartment(
            Long evaluationsId, Members loginMember){
        Evaluations evaluation = findById(evaluationsId);
        membersService.findById(loginMember.getId());

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneForCompanyDto readOneForCompany(
            Long evaluationId, Members loginMember){
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Evaluations findById(Long evaluationId){
        return evaluationsRepository.findById(evaluationId).orElseThrow(
                ()-> new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATIONS));
    }

    private Integer validateAndDetermineQuarter(LocalDateTime startDate, LocalDateTime endDate) {
        // 1. 시작 날이 지난 날짜로 설정될 때
        if(startDate.isBefore(LocalDateTime.now())){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.NOW_DATE_BEFORE_START_DATE);
        }
        // 2. 시작 날이 마감 날보다 느릴 때
        if (startDate.isBefore(endDate)) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.END_DATE_BEFORE_START_DATE);
        }
        // 3. 최소 기간 보장 7일
        if (ChronoUnit.DAYS.between(startDate, endDate) < 7) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.MINIMUM_DURATION_TOO_SHORT);
        }
        // 4. 시작 분기와 마감 분기가 일치하는지?
        int startQuarter = (startDate.getMonthValue() - 1) / 3 + 1;
        int endQuarter = (endDate.getMonthValue() - 1) / 3 + 1;
        if (endQuarter > startQuarter) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.INVALID_QUARTER_REQUEST);
        }
        // 5. 최소 분기별 마지노선 마감 날짜일: 분기별 마지막 달 마지막 일의 14일 전
        LocalDateTime quarterEndDate = getQuarterEndDate(startQuarter).minusDays(14);
        if (endDate.isAfter(quarterEndDate)) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.END_DATE_TOO_LATE);
        }
        return startQuarter;
    }

    private LocalDateTime getQuarterEndDate(Integer quarter) {
        int nowYear = LocalDateTime.now().getYear();
        return switch (quarter) {
            case 1 -> LocalDateTime.of(nowYear, 3, 31, 23, 59, 59);
            case 2 -> LocalDateTime.of(nowYear, 6, 30, 23, 59, 59);
            case 3 -> LocalDateTime.of(nowYear, 9, 30, 23, 59, 59);
            case 4 -> LocalDateTime.of(nowYear, 12, 31, 23, 59, 59);
            default -> throw new EvaluationsCustomException(EvaluationsExceptionCode.QUARTER_CALCULATE_ERROR);
        };
    }
}
