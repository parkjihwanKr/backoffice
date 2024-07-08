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
import com.example.backoffice.domain.memberEvaluation.converter.MembersEvaluationsConverter;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.service.MembersEvaluationsServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacade;
import com.example.backoffice.domain.question.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationsServiceV1Impl implements EvaluationsServiceV1{

    private final MembersServiceV1 membersService;
    private final NotificationsServiceFacade notificationsServiceFacade;
    private final EvaluationsRepository evaluationsRepository;
    private final MembersEvaluationsServiceV1 membersEvaluationsService;

    @Override
    @Transactional
    public EvaluationsResponseDto.CreateOneForDepartmentDto createOneForDepartment(
            Members loginMember, EvaluationsRequestDto.CreateOneForDepartmentDto requestDto){

        // 1. 적절한 부서에서 설문 조사를 만들었는지?
        MemberDepartment department = MembersConverter.toDepartment(requestDto.getDepartment());
        if(!loginMember.getDepartment().equals(department)
                && (loginMember.getPosition().equals(MemberPosition.MANAGER)
                || loginMember.getPosition().equals(MemberPosition.CEO))){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_DEPARTMENT_ACCESS);
        }
        // 2. 요청한 날짜가 시작, 마감 날짜가 분기에 따라 잘 나뉘었는지?
        Integer quarter
                = validateAndDetermineQuarter(
                        requestDto.getStartDate(), requestDto.getEndDate());

        int year = requestDto.getStartDate().getYear();
        String title = year+"년 "+quarter+"분기 부서 설문조사";

        // 한 평가에 여럿 멤버, 한 멤버에 여럿 평가 가능. 다대다인데?
        // 일단 부서에 대한 평가를 부서원들에게 전달해야하니까 이건 맞음
        List<Members> memberList = membersService.findAllByDepartment(department);

        Evaluations evaluation
                = EvaluationsConverter.toEntity(
                        title, year, quarter, requestDto.getDescription(),
                loginMember.getDepartment(), requestDto.getStartDate(), requestDto.getEndDate());
        evaluationsRepository.save(evaluation);

        sendNotificationForMemberList(loginMember, memberList, title, evaluation);

        return EvaluationsConverter.toCreateOneForDepartmentDto(
                title, evaluation.getDescription(),loginMember.getMemberName(),
                evaluation.getStartDate(), evaluation.getEndDate());
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.CreateOneForCompanyDto createOneForCompany(
            Members loginMember, EvaluationsRequestDto.CreateOneForCompanyDto requestDto){
        // 1. 인사부장과 일치하는 인물인지? 또는 ceo인지?
        if((!loginMember.getDepartment().equals(MemberDepartment.HR) && loginMember.getPosition().equals(MemberPosition.MANAGER))
                || !loginMember.getPosition().equals(MemberPosition.CEO)){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_ACCESS);
        }

        // 2. startDate, endDate 검증
        validateDate(requestDto.getStartDate(), requestDto.getEndDate());

        // 3. toEntity
        int year = requestDto.getStartDate().getYear();
        String title = year + "년도 회사 내부 평가";
        List<Members> memberList = membersService.findAll();
        Evaluations evaluation
                = EvaluationsConverter.toEntity(
                title, year, 1, requestDto.getDescription(),
                loginMember.getDepartment(), requestDto.getStartDate(),
                requestDto.getEndDate());
        evaluationsRepository.save(evaluation);

        // 4. 모든 멤버에게 작성 요청
        sendNotificationForMemberList(loginMember, memberList, title, evaluation);

        // 5. responseDto 작성
        return EvaluationsConverter.toCreateOneForCompanyDto(
                loginMember.getMemberName(), title, evaluation.getDescription(),
                evaluation.getStartDate(), evaluation.getEndDate());
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneForDepartmentDto readOneForDepartment(
            Integer year, Integer quarter, Long evaluationsId, Members loginMember){
        Evaluations evaluation = findById(evaluationsId);

        // 부서 설문 조사에 부서원들, 사장만 접근 가능
        if(!evaluation.getDepartment().equals(loginMember.getDepartment())){
            if(!loginMember.getPosition().equals(MemberPosition.CEO)){
                throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_ACCESS);
            }
        }

        membersEvaluationsService.findByMemberIdAndEvaluationId(loginMember.getId(), evaluationsId);

        return EvaluationsConverter.toReadOneForDepartmentDto(
                evaluation.getTitle(), year, quarter, loginMember.getMemberName(),
                evaluation.getQuestionList());
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneForCompanyDto readOneForCompany(
            Integer year, Long evaluationId, Members loginMember){

        Evaluations evaluation = findById(evaluationId);

        membersEvaluationsService.findByMemberIdAndEvaluationId(loginMember.getId(), evaluationId);

        return EvaluationsConverter.toReadOneForCompanyDto(
                evaluation.getTitle(), evaluation.getDescription(), evaluation.getYear());
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.UpdateOneForDepartmentDto updateOneForDepartment(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForDepartmentDto requestDto){
        Evaluations evaluation = findById(evaluationId);
        // 1. 해당 설문 조사를 변경할 권한 검증
        if(!loginMember.getDepartment().equals(evaluation.getDepartment())
                || !loginMember.getPosition().equals(MemberPosition.MANAGER)){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_ACCESS);
        }

        // 2. requestDto의 startDate와 endDate 검증
        Integer quarter
                = validateAndDetermineQuarter(
                        requestDto.getStartDate(), requestDto.getEndDate());

        // 3. 해당 부서의 멤버에게 알림 발송
        List<Members> memberList
                = membersService.findAllByDepartment(loginMember.getDepartment());
        String message = requestDto.getTitle();
        sendNotificationForMemberList(loginMember, memberList, message, evaluation);

        // 4. 요청 사항에 따른 엔티티 변경
        evaluation.update(
                requestDto.getTitle(), requestDto.getDescription(),
                requestDto.getStartDate(), requestDto.getEndDate(),
                requestDto.getStartDate().getYear());

        // 5. 응답 DTO
        return EvaluationsConverter.toUpdateOneForDepartmentDto(
                evaluation.getDepartment(), evaluation.getTitle(), evaluation.getDescription(),
                evaluation.getYear(), evaluation.getQuarter(), loginMember.getMemberName(),
                evaluation.getStartDate(), evaluation.getEndDate());
    }

    @Override
    @Transactional(readOnly = true)
    public Evaluations findById(Long evaluationId){
        return evaluationsRepository.findById(evaluationId).orElseThrow(
                ()-> new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATIONS));
    }

    // 1. 시작 날짜가 다음년도 1분기일 때, 이번 년도 4분기에만 가능하게 변경
    // 2. 시작 날짜가 다음년도 2~4분기일 때, 이번 년도의 1~3분기에만 가능하게
    // 즉, 설문 조사를 시작하는 분기의 전 분기에만 만들 수 있게 하고자 함.
    private Integer validateAndDetermineQuarter(LocalDate startDate, LocalDate endDate) {
        // 1. startDate, endDate 검증
        validateDate(startDate, endDate);

        // 2. 시작 분기와 마감 분기가 일치하는지?
        int startQuarter = (startDate.getMonthValue() - 1) / 3 + 1;
        int endQuarter = (endDate.getMonthValue() - 1) / 3 + 1;
        if (endQuarter > startQuarter) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.INVALID_QUARTER_REQUEST);
        }
        // 3. 최소 분기별 마지노선 마감 날짜일: 분기별 마지막 달 마지막 일의 14일 전
        LocalDate quarterEndDate = getQuarterEndDate(startQuarter).minusDays(14);
        if (endDate.isAfter(quarterEndDate)) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.END_DATE_TOO_LATE);
        }
        return startQuarter;
    }

    private LocalDate getQuarterEndDate(Integer quarter) {
        int nowYear = LocalDate.now().getYear();
        return switch (quarter) {
            case 1 -> LocalDate.of(nowYear, 3, 31);
            case 2 -> LocalDate.of(nowYear, 6, 30);
            case 3 -> LocalDate.of(nowYear, 9, 30);
            case 4 -> LocalDate.of(nowYear, 12, 31);
            default -> throw new EvaluationsCustomException(EvaluationsExceptionCode.QUARTER_CALCULATE_ERROR);
        };
    }

    private void validateDate(LocalDate startDate, LocalDate endDate){
        // 1. 해당 startDate, endDate의 연도가 같은지?
        int nowYear = LocalDateTime.now().getYear();
        if(startDate.getYear() != nowYear || endDate.getYear() != nowYear){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.NOT_SAME_NOW_YEAR);
        }

        // 2. 시작 날이 지난 날짜로 설정될 때
        if(startDate.isBefore(LocalDate.now())){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.NOW_DATE_BEFORE_START_DATE);
        }

        // 3. 시작 날이 마감 날보다 느릴 때
        if (!startDate.isBefore(endDate)) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.END_DATE_BEFORE_START_DATE);
        }

        // 4. 최소 기간 보장 7일
        if (ChronoUnit.DAYS.between(startDate, endDate) < 7) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.MINIMUM_DURATION_TOO_SHORT);
        }
    }

    private void sendNotificationForMemberList(
            Members loginMember, List<Members> memberList, String message, Evaluations evaluation){
        for(Members member : memberList){
            notificationsServiceFacade.createNotification(
                    NotificationsConverter.toNotificationData(
                            loginMember, member, null, null,
                            null, null, message),
                    NotificationType.EVALUATION);

            membersEvaluationsService.save(
                    MembersEvaluationsConverter.toEntity(member, evaluation));
        }
    }
}
