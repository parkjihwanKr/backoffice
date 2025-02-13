package com.example.backoffice.domain.evaluation.facade;

import com.example.backoffice.domain.evaluation.converter.EvaluationsConverter;
import com.example.backoffice.domain.evaluation.dto.EvaluationsRequestDto;
import com.example.backoffice.domain.evaluation.dto.EvaluationsResponseDto;
import com.example.backoffice.domain.evaluation.entity.EvaluationType;
import com.example.backoffice.domain.evaluation.entity.Evaluations;
import com.example.backoffice.domain.evaluation.exception.EvaluationsCustomException;
import com.example.backoffice.domain.evaluation.exception.EvaluationsExceptionCode;
import com.example.backoffice.domain.evaluation.service.EvaluationsServiceV1;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.MemberPosition;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.memberEvaluation.converter.MembersEvaluationsConverter;
import com.example.backoffice.domain.memberEvaluation.entity.MembersEvaluations;
import com.example.backoffice.domain.memberEvaluation.service.MembersEvaluationsServiceV1;
import com.example.backoffice.domain.notification.converter.NotificationsConverter;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.facade.NotificationsServiceFacadeV1;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.service.QuestionsServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EvaluationsServiceFacadeV1Impl implements EvaluationsServiceFacadeV1{

    private final MembersServiceV1 membersService;
    private final NotificationsServiceFacadeV1 notificationsServiceFacade;
    private final MembersEvaluationsServiceV1 membersEvaluationsService;
    private final QuestionsServiceV1 questionsService;
    private final EvaluationsServiceV1 evaluationsService;

    @Override
    @Transactional
    public EvaluationsResponseDto.CreateOneForDepartmentDto createOneDepartmentType(
            Members loginMember, EvaluationsRequestDto.CreateOneForDepartmentDto requestDto){

        // 1. 적절한 부서에서 설문 조사를 만들었는지?
        MemberDepartment department
                = membersService.findDepartment(requestDto.getDepartment());
        matchDepartmentManager(
                department, loginMember.getDepartment(), loginMember.getPosition());

        // 2. 요청한 날짜가 시작, 마감 날짜가 분기에 따라 잘 나뉘었는지?
        Integer quarter
                = validateAndDetermineQuarter(
                requestDto.getStartDate(), requestDto.getEndDate());

        int year = requestDto.getStartDate().getYear();

        String title = requestDto.getTitle();
        if(title.isBlank()){
            title = year+"년 "+quarter+"분기 부서 설문조사";
        }

        List<Members> memberList = membersService.findAllByDepartment(department);

        Evaluations evaluation
                = EvaluationsConverter.toEntity(
                title, year, quarter, requestDto.getDescription(),
                loginMember.getDepartment(), requestDto.getStartDate(),
                requestDto.getEndDate(), EvaluationType.DEPARTMENT);
        evaluationsService.save(evaluation);

        sendNotificationForMemberList(
                loginMember, memberList, title, evaluation, NotificationType.EVALUATION);

        return EvaluationsConverter.toCreateOneForDepartmentDto(
                evaluation.getId(), title, evaluation.getDescription(),loginMember.getMemberName(),
                evaluation.getStartDate(), evaluation.getEndDate());
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.CreateOneForCompanyDto createOneCompanyType(
            Members loginMember, EvaluationsRequestDto.CreateOneForCompanyDto requestDto){
        // 1. 인사부장과 일치하는 인물인지? 또는 ceo인지?
        matchHRManagerOrCEO(loginMember.getDepartment(), loginMember.getPosition());

        // 2. startDate, endDate 검증
        validateDate(requestDto.getStartDate(), requestDto.getEndDate());

        // 3. toEntity
        int year = requestDto.getStartDate().getYear();
        String title = requestDto.getTitle();
        if(title.isBlank()){
            title = year + "년도 회사 내부 평가";
        }

        List<Members> memberList = membersService.findAll();
        Evaluations evaluation
                = EvaluationsConverter.toEntity(
                title, year, 1, requestDto.getDescription(),
                loginMember.getDepartment(), requestDto.getStartDate(),
                requestDto.getEndDate(), EvaluationType.COMPANY);
        evaluationsService.save(evaluation);

        // 4. 모든 멤버에게 작성 요청
        sendNotificationForMemberList(
                loginMember, memberList, title, evaluation, NotificationType.EVALUATION);

        // 5. responseDto 작성
        return EvaluationsConverter.toCreateOneForCompanyDto(
                evaluation.getId(), loginMember.getMemberName(), title, evaluation.getDescription(),
                evaluation.getStartDate(), evaluation.getEndDate());
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneForDepartmentDto readOneDepartmentType(
            Integer year, Integer quarter, Long evaluationId, Members loginMember){
        Evaluations evaluation
                = evaluationsService.findByIdAndEvaluationType(
                        evaluationId, EvaluationType.DEPARTMENT);

        matchDepartmentOrCEO(
                evaluation.getDepartment(), loginMember.getDepartment(),
                loginMember.getPosition());

        return EvaluationsConverter.toReadOneForDepartmentDto(
                evaluationId, evaluation.getTitle(), evaluation.getDescription(),
                year, quarter, loginMember.getMemberName(),
                evaluation.getQuestionList());
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneForCompanyDto readOneCompanyType(
            Integer year, Long evaluationId, Members loginMember){

        Evaluations evaluation
                = evaluationsService.findByIdAndEvaluationType(
                evaluationId, EvaluationType.COMPANY);

        return EvaluationsConverter.toReadOneForCompanyDto(
                evaluationId, evaluation.getTitle(), evaluation.getDescription(), evaluation.getYear(),
                loginMember.getMemberName(), evaluation.getQuestionList());
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.UpdateOneForDepartmentDto updateOneDepartmentType(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForDepartmentDto requestDto){
        Evaluations evaluation
                = evaluationsService.findByIdAndEvaluationType(
                evaluationId, EvaluationType.DEPARTMENT);
        // 1. 해당 설문 조사를 변경할 권한 검증
        matchDepartmentManager(
                evaluation.getDepartment(), loginMember.getDepartment(), loginMember.getPosition());

        // 2. requestDto의 startDate와 endDate 검증
        Integer quarter
                = validateAndDetermineQuarter(
                requestDto.getStartDate(), requestDto.getEndDate());

        // 3. 해당 부서의 멤버에게 알림 발송
        List<Members> memberList
                = membersService.findAllByDepartment(loginMember.getDepartment());
        sendNotificationForMemberList(
                loginMember, memberList, requestDto.getTitle(),
                evaluation, NotificationType.UPDATE_EVALUATION);

        // 4. 요청 사항에 따른 엔티티 변경
        evaluation.update(
                requestDto.getTitle(), requestDto.getDescription(),
                requestDto.getStartDate(), requestDto.getEndDate(),
                requestDto.getStartDate().getYear(), quarter);

        // 5. 응답 DTO
        return EvaluationsConverter.toUpdateOneForDepartmentDto(
                evaluationId, evaluation.getDepartment(), evaluation.getTitle(),
                evaluation.getDescription(), evaluation.getYear(),
                evaluation.getQuarter(), loginMember.getName(),
                evaluation.getStartDate(), evaluation.getEndDate());
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.UpdateOneForCompanyDto updateOneCompanyType(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneForCompanyDto requestDto){
        // 1. 존재하는 평가인지?
        Evaluations evaluation
                = evaluationsService.findByIdAndEvaluationType(
                        evaluationId, EvaluationType.COMPANY);

        // 2. 평가를 수정할 수 있는 권한인지?
        if(!(loginMember.getPosition().equals(MemberPosition.MANAGER)
                && loginMember.getDepartment().equals(MemberDepartment.HR))
                && !loginMember.getPosition().equals(MemberPosition.CEO)){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_ACCESS);
        }

        // 3. 시작일과 마감일을 적절하게 요청했는지?
        validateDate(requestDto.getStartDate(), requestDto.getEndDate());

        // 4. 알림 전송
        sendNotificationForMemberList(
                loginMember, membersService.findAll(), requestDto.getTitle(),
                evaluation, NotificationType.UPDATE_EVALUATION);

        // 5. 평가 엔티티 수정
        evaluation.update(
                requestDto.getTitle(), requestDto.getDescription(),
                requestDto.getStartDate(), requestDto.getEndDate(),
                requestDto.getStartDate().getYear(), 1);

        // 6. 응답 DTO 생성
        return EvaluationsConverter.toUpdateOneForCompanyDto(
                evaluationId, evaluation.getTitle(), evaluation.getDescription(),
                evaluation.getYear(), evaluation.getStartDate(),
                evaluation.getEndDate(), loginMember.getMemberName());
    }

    @Override
    @Transactional
    public void deleteOne(Long evaluationId, Members loginMember){
        // 1. 평가가 존재하는지?
        Evaluations evaluation = evaluationsService.findById(evaluationId);
        EvaluationType evaluationType = evaluation.getEvaluationType();
        switch (evaluationType) {
            case DEPARTMENT -> {
                matchDepartmentManager(
                        evaluation.getDepartment(), loginMember.getDepartment(),
                        loginMember.getPosition());
                evaluationsService.deleteById(evaluationId);
            }
            case COMPANY -> {
                matchHRManagerOrCEO(loginMember.getDepartment(), loginMember.getPosition());
                evaluationsService.deleteById(evaluationId);
            }
            default -> throw new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
        }
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.SubmitOneDto submitOne(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.SubmitOneDto requestDto){
        // 로그인 멤버가 진행할 수 있는 설문 조사인지?
        MembersEvaluations membersEvaluation
                = membersEvaluationsService.findByMemberIdAndEvaluationId(
                loginMember.getId(), evaluationId);

        for(QuestionsRequestDto.SubmitOneDto questionRequestDto : requestDto.getQuestionSubmitAllDto()){
            // 해당 Question이 evaluation에 포함되어있는지?
            questionsService.submitOne(
                    evaluationId, questionRequestDto.getQuestionNumber(),
                    questionRequestDto, loginMember);
        }
        if(!requestDto.getChecked()){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.NOT_INPUT_EVALUATION_CHECKED);
        }
        membersEvaluation.isCompleted(true);
        return EvaluationsConverter.toSubmitOneDto(evaluationId, loginMember.getMemberName());
    }

    @Override
    @Transactional
    public void deleteSubmittedOneForCancellation(Long evaluationId, Members loginMember){
        // 해당 멤버에게 할당된 설문조사가 있는지?
        membersEvaluationsService.deleteSubmittedOneForCancellation(evaluationId, loginMember.getId());
    }

    private void matchDepartmentManager(
            MemberDepartment department, MemberDepartment loginMemberDepartment,
            MemberPosition loginMemberPosition){
        if(!loginMemberDepartment.equals(department)
                && (loginMemberPosition.equals(MemberPosition.MANAGER)
                || loginMemberPosition.equals(MemberPosition.CEO))){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_DEPARTMENT_ACCESS);
        }
    }

    private void matchHRManagerOrCEO(MemberDepartment department, MemberPosition position){
        if((!department.equals(MemberDepartment.HR) && position.equals(MemberPosition.MANAGER))
                || !position.equals(MemberPosition.CEO)){
            throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_ACCESS);
        }
    }

    private void matchDepartmentOrCEO(
            MemberDepartment evaluationDepartment, MemberDepartment loginMemberDepartment,
            MemberPosition loginMemberPosition){
        // 부서 설문 조사에 부서원들, 사장만 접근 가능
        if(!evaluationDepartment.equals(loginMemberDepartment)){
            if(!loginMemberPosition.equals(MemberPosition.CEO)){
                throw new EvaluationsCustomException(EvaluationsExceptionCode.UNAUTHORIZED_ACCESS);
            }
        }
    }

    // 1. 시작 날짜가 다음년도 1분기일 때, 이번 년도 4분기에만 가능하게 변경
    // 2. 시작 날짜가 다음년도 2~4분기일 때, 이번 년도의 1~3분기에만 가능하게
    // 즉, 설문 조사를 시작하는 분기의 전 분기에만 만들 수 있게 하고자 함.
    private Integer validateAndDetermineQuarter(LocalDate startDate, LocalDate endDate) {
        // 1. startDate, endDate 검증
        validateDate(startDate, endDate);

        // 2. 시작 분기와 마감 분기가 일치하는지?
        // int startQuarter = (startDate.getMonthValue() - 1) / 3 + 1;
        /*int endQuarter = (endDate.getMonthValue() - 1) / 3 + 1;
        if (endQuarter > startQuarter) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.INVALID_QUARTER_REQUEST);
        }*/
        // 2. 최소 분기별 마지노선 마감 날짜일: 분기별 마지막 달 마지막 일의 14일 전
        int startQuarter = (startDate.getMonthValue() - 1) / 3 + 1;
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

        // 5. 시작 분기와 마감 분기가 일치하는지?
        int startQuarter = (startDate.getMonthValue() - 1) / 3 + 1;
        int endQuarter = (endDate.getMonthValue() - 1) / 3 + 1;
        if (endQuarter > startQuarter) {
            throw new EvaluationsCustomException(EvaluationsExceptionCode.INCORRECT_DATE_QUARTER_REQUEST);
        }
    }

    private void sendNotificationForMemberList(
            Members loginMember, List<Members> memberList, String message,
            Evaluations evaluation, NotificationType notificationType){
        switch (notificationType) {
            case EVALUATION, UPDATE_EVALUATION -> {
                for(Members member : memberList){
                    notificationsServiceFacade.createOne(
                            NotificationsConverter.toNotificationData(
                                    loginMember, member, null, null,
                                    null, null, message),
                            notificationType);

                    membersEvaluationsService.save(
                            MembersEvaluationsConverter.toEntity(member, evaluation));
                }
            }
            default -> throw new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
        }
    }
}
