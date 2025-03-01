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
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.domain.question.dto.QuestionsRequestDto;
import com.example.backoffice.domain.question.service.QuestionsServiceV1;
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EvaluationsServiceFacadeImplV1 implements EvaluationsServiceFacadeV1{

    private final MembersServiceV1 membersService;
    private final NotificationsServiceV1 notificationsService;
    private final MembersEvaluationsServiceV1 membersEvaluationsService;
    private final QuestionsServiceV1 questionsService;
    private final EvaluationsServiceV1 evaluationsService;

    @Override
    @Transactional
    public EvaluationsResponseDto.CreateOneDto createOne(
            Members loginMember, EvaluationsRequestDto.CreateOneDto requestDto){

        // 1. 적절한 타입의 설문 조사를 만들었는지?
        EvaluationType requestedEvaluationType
                = EvaluationsConverter.toEvaluationType(
                        requestDto.getEvaluationType());

        switch (requestedEvaluationType) {
            case DEPARTMENT -> {
                return createOneDepartmentType(requestDto, loginMember, requestedEvaluationType);
            }
            case COMPANY -> {
                return createOneCompanyType(requestDto, loginMember, requestedEvaluationType);
            }
            default -> {
                throw new EvaluationsCustomException(
                        EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationsResponseDto.ReadOneDto readOne(
            Integer year, Integer quarter, String evaluationType, Long evaluationId, Members loginMember){
        EvaluationType requestEvaluationType
                = EvaluationsConverter.toEvaluationType(evaluationType);
        switch (requestEvaluationType) {
            case DEPARTMENT -> {
                return toReadOneDepartmentType(evaluationId, loginMember, year, quarter);
            }
            case COMPANY-> {
                return toReadOneCompanyType(evaluationId, loginMember);
            }
            default -> throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
        }
    }

    @Override
    @Transactional
    public EvaluationsResponseDto.UpdateOneDto updateOne(
            Long evaluationId, Members loginMember,
            EvaluationsRequestDto.UpdateOneDto requestDto){
        LocalDate startDate = DateTimeUtils.parse(requestDto.getStartDate()).toLocalDate();
        LocalDate endDate = DateTimeUtils.parse(requestDto.getEndDate()).toLocalDate();

        Evaluations evaluation = evaluationsService.findById(evaluationId);

        switch (evaluation.getEvaluationType()) {
            case DEPARTMENT -> {
                // 1. 해당 설문 조사를 변경할 권한 검증
                matchDepartmentManager(
                        evaluation.getDepartment(),
                        loginMember.getDepartment(), loginMember.getPosition());

                // 2. requestDto의 startDate와 endDate 검증
                Integer quarter
                        = validateAndDetermineQuarter(startDate, endDate);

                // 3. 요청 사항에 따른 엔티티 변경
                evaluation.update(
                        requestDto.getTitle(), requestDto.getDescription(),
                        startDate, endDate, startDate.getYear(), quarter);

                // 4. 해당 부서의 멤버에게 알림 발송
                List<Members> memberList
                        = membersService.findAllByDepartment(loginMember.getDepartment());
                sendNotificationForMemberList(
                        loginMember, memberList, requestDto.getTitle(),
                        evaluation, NotificationType.UPDATE_EVALUATION);

                // 5. 응답 DTO
                return EvaluationsConverter.toUpdateOneDto(
                        evaluationId, evaluation.getDepartment(), evaluation.getTitle(),
                        evaluation.getDescription(), evaluation.getYear(),
                        evaluation.getQuarter(), loginMember.getName(),
                        evaluation.getStartDate(), evaluation.getEndDate());
            }
            case COMPANY -> {
                // 1. 설문조사를 수정할 수 있는 권한인지?
                matchHRManagerOrCEO(loginMember.getDepartment(), loginMember.getPosition());

                // 2. 시작일과 마감일을 적절하게 요청했는지?
                validateDate(startDate, endDate);

                // 3. 알림 전송
                sendNotificationForMemberList(
                        loginMember, membersService.findAll(), requestDto.getTitle(),
                        evaluation, NotificationType.UPDATE_EVALUATION);

                // 4. 평가 엔티티 수정
                evaluation.update(
                        requestDto.getTitle(), requestDto.getDescription(),
                        startDate, endDate, startDate.getYear(), 1);

                // 5. 응답 DTO 생성
                return EvaluationsConverter.toUpdateOneDto(
                        evaluationId, null,
                        evaluation.getTitle(), evaluation.getDescription(),
                        evaluation.getYear(), 1,
                        loginMember.getMemberName(),
                        evaluation.getStartDate(),
                        evaluation.getEndDate());
            }
            default -> throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
        }
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
            default ->
                    throw new EvaluationsCustomException(
                            EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
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
            MemberDepartment requestedDepartment, MemberDepartment loginMemberDepartment,
            MemberPosition loginMemberPosition){
        if(!loginMemberDepartment.equals(requestedDepartment)
                && (loginMemberPosition.equals(MemberPosition.MANAGER)
                || loginMemberPosition.equals(MemberPosition.CEO))){
            throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.UNAUTHORIZED_DEPARTMENT_ACCESS);
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
        if(!DateTimeUtils.isYearEqualToNow(startDate, endDate)){
            throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.IS_NOT_YAER_EQUAL_TO_NOW);
        }

        // 2. 시작 날이 지난 날짜로 설정될 때
        if(startDate.isBefore(DateTimeUtils.getCurrentDateTime().toLocalDate())){
            throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.NOW_DATE_BEFORE_START_DATE);
        }

        // 3. 시작 날이 마감 날보다 느릴 때
        DateTimeUtils.validateStartAndEndDate(
                startDate.atTime(0,0,0),
                endDate.atTime(0,0,0));

        // 4. 최소 기간 보장 7일
        if (DateTimeUtils.isAtLeastSevenDaysDuration(startDate, endDate)) {
            throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.MINIMUM_DURATION_TOO_SHORT);
        }

        // 5. 시작 분기와 마감 분기가 일치하는지?
        int startQuarter = (startDate.getMonthValue() - 1) / 3 + 1;
        int endQuarter = (endDate.getMonthValue() - 1) / 3 + 1;
        if (endQuarter > startQuarter) {
            throw new EvaluationsCustomException(
                    EvaluationsExceptionCode.INCORRECT_DATE_QUARTER_REQUEST);
        }
    }

    private void sendNotificationForMemberList(
            Members loginMember, List<Members> memberList, String message,
            Evaluations evaluation, NotificationType notificationType){
        switch (notificationType) {
            case EVALUATION, UPDATE_EVALUATION -> {
                for(Members member : memberList){
                    notificationsService.generateEntityAndSendMessage(
                            NotificationsConverter.toNotificationData(
                                    loginMember, member, message),
                            notificationType);

                    membersEvaluationsService.save(
                            MembersEvaluationsConverter.toEntity(member, evaluation));
                }
            }
            default -> throw new EvaluationsCustomException(EvaluationsExceptionCode.NOT_FOUND_EVALUATION_TYPE);
        }
    }

    private EvaluationsResponseDto.CreateOneDto createOneDepartmentType(
            EvaluationsRequestDto.CreateOneDto requestDto, Members loginMember,
            EvaluationType evaluationType) {
        MemberDepartment requestedDepartment
                = membersService.findDepartment(requestDto.getDepartment());
        matchDepartmentManager(
                requestedDepartment, loginMember.getDepartment(), loginMember.getPosition());

        // 2. 요청한 날짜가 시작, 마감 날짜가 분기에 따라 잘 나뉘었는지?
        LocalDate startDate = DateTimeUtils.parse(requestDto.getStartDate()).toLocalDate();
        LocalDate endDate = DateTimeUtils.parse(requestDto.getEndDate()).toLocalDate();

        Integer quarter
                = validateAndDetermineQuarter(startDate, endDate);

        int year = startDate.getYear();

        String title = requestDto.getTitle();
        if(title.isBlank()){
            title = year+"년 "+quarter+"분기 부서 설문조사";
        }

        List<Members> memberList
                = membersService.findAllByDepartment(requestedDepartment);

        Evaluations evaluation
                = EvaluationsConverter.toEntity(
                title, year, quarter, requestDto.getDescription(),
                loginMember.getDepartment(), startDate, endDate, EvaluationType.DEPARTMENT);
        evaluationsService.save(evaluation);

        sendNotificationForMemberList(
                loginMember, memberList, title, evaluation, NotificationType.EVALUATION);

        return EvaluationsConverter.toCreateOneDto(
                evaluation.getId(), title, evaluation.getDescription(),
                loginMember.getMemberName(), evaluation.getStartDate(),
                evaluation.getEndDate(), evaluationType, requestedDepartment);
    }

    private EvaluationsResponseDto.CreateOneDto createOneCompanyType(
            EvaluationsRequestDto.CreateOneDto requestDto,
            Members loginMember, EvaluationType evaluationType){
        // 1. 인사부장과 일치하는 인물인지? 또는 ceo인지?
        matchHRManagerOrCEO(loginMember.getDepartment(), loginMember.getPosition());

        LocalDate startDate = DateTimeUtils.parse(requestDto.getStartDate()).toLocalDate();
        LocalDate endDate = DateTimeUtils.parse(requestDto.getEndDate()).toLocalDate();
        // 2. startDate, endDate 검증
        validateDate(startDate, endDate);

        // 3. toEntity
        int year = startDate.getYear();
        String title = requestDto.getTitle();
        if(title.isBlank()){
            title = year + "년도 회사 내부 평가";
        }

        List<Members> memberList = membersService.findAll();
        Evaluations evaluation
                = EvaluationsConverter.toEntity(
                title, year, 1, requestDto.getDescription(),
                null, startDate, endDate, EvaluationType.COMPANY);
        evaluationsService.save(evaluation);

        // 4. 모든 멤버에게 작성 요청
        sendNotificationForMemberList(
                loginMember, memberList, title, evaluation, NotificationType.EVALUATION);

        // 5. responseDto 작성
        return EvaluationsConverter.toCreateOneDto(
                evaluation.getId(), loginMember.getMemberName(), title, evaluation.getDescription(),
                evaluation.getStartDate(), evaluation.getEndDate(), evaluationType, null);
    }

    private EvaluationsResponseDto.ReadOneDto toReadOneDepartmentType(
            Long evaluationId, Members loginMember, Integer year, Integer quarter){
        Evaluations evaluation
                = evaluationsService.findByIdAndEvaluationType(
                evaluationId, EvaluationType.DEPARTMENT);

        matchDepartmentOrCEO(
                evaluation.getDepartment(), loginMember.getDepartment(),
                loginMember.getPosition());

        return EvaluationsConverter.toReadOneDto(
                evaluationId, evaluation.getTitle(),
                evaluation.getDescription(), year,
                quarter, loginMember.getMemberName(),
                evaluation.getQuestionList(), evaluation.getEvaluationType());
    }

    private EvaluationsResponseDto.ReadOneDto toReadOneCompanyType(
            Long evaluationId, Members loginMember){
        Evaluations evaluation
                = evaluationsService.findByIdAndEvaluationType(
                evaluationId, EvaluationType.COMPANY);

        return EvaluationsConverter.toReadOneDto(
                evaluationId, evaluation.getTitle(),
                evaluation.getDescription(), evaluation.getYear(),
                1, loginMember.getMemberName(),
                evaluation.getQuestionList(), evaluation.getEvaluationType());
    }
}
