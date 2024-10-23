package com.example.backoffice.domain.expense.service;

import com.example.backoffice.domain.expense.converter.ExpenseConverter;
import com.example.backoffice.domain.expense.dto.ExpenseRequestDto;
import com.example.backoffice.domain.expense.dto.ExpenseResponseDto;
import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.domain.expense.entity.ExpenseProcess;
import com.example.backoffice.domain.expense.exception.ExpenseCustomException;
import com.example.backoffice.domain.expense.exception.ExpenseExceptionCode;
import com.example.backoffice.domain.expense.repository.ExpenseRepository;
import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.file.service.FilesServiceV1;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.notification.entity.NotificationData;
import com.example.backoffice.domain.notification.entity.NotificationType;
import com.example.backoffice.domain.notification.service.NotificationsServiceV1;
import com.example.backoffice.global.date.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImplV1 implements ExpenseServiceV1{

    private final MembersServiceV1 membersService;
    private final FilesServiceV1 filesService;
    private final NotificationsServiceV1 notificationsService;
    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public ExpenseResponseDto.CreateOneDto createOne(
            String department, Members loginMember,
            ExpenseRequestDto.CreateOneDto requestDto,
            List<MultipartFile> multipartFileList){
        // 1. 만들 수 있는 부서인지?
        MemberDepartment memberDepartment
                = membersService.findDepartment(department);
        if(loginMember.getDepartment().equals(memberDepartment)){
            throw new ExpenseCustomException(ExpenseExceptionCode.DO_NOT_CREATE_EXPENSE_DEPARTMENT);
        }

        Expense expense = ExpenseConverter.toEntity(
                requestDto.getTitle(), requestDto.getMoney(),
                requestDto.getDetails(), memberDepartment);
        expenseRepository.save(expense);

        List<Files> fileList = new ArrayList<>();
        for(MultipartFile files : multipartFileList){
            Files file = filesService.createOneForExpense(files, expense, loginMember);
            fileList.add(file);
        }

        // financeManger가 없으면 null을 출력
        Members financeManger = membersService.findByFinanceManager();
        NotificationData notificationData;
        String message
                = loginMember.getDepartment().getDepartment()
                +"부서의 지출 내역서 승인 요청";
        if (financeManger != null) {
            notificationData
                    = notificationsService.toNotificationData(
                    financeManger, loginMember,null,
                    null, null, null, message);
        }else{
            // 무조건 존재
            Members ceo = membersService.findById(1L);
            notificationData
                    = notificationsService.toNotificationData(
                    ceo, loginMember,null,
                    null, null, null, message);
        }
        notificationsService.generateEntityAndSendMessage(
                notificationData, NotificationType.CREATE_EXPENSE_REPORT);

        return ExpenseConverter.toCreateOneDto(
                expense, fileList, loginMember.getMemberName());
    }

    @Override
    @Transactional
    public ExpenseResponseDto.UpdateOneForProcessDto updateOneForProcess(
            Long expenseId, String department, String process, Members loginMember){
        // 1. 존재하는 부서인지
        MemberDepartment memberDepartment = membersService.findDepartment(department);

        // 2. 바꿀 수 있는 권한이 있는지?
        membersService.findByFinanceManagerOrCeo(loginMember.getId());

        // 3. 존재하는 비용 내역서인지?
        Expense expense = findById(expenseId);
        if(expense.getDepartment().equals(memberDepartment)){
            throw new ExpenseCustomException(ExpenseExceptionCode.NOT_MATCHED_EXPENSE_DEPARTMENT);
        }

        // 4. process 변경
        ExpenseProcess expenseProcess = ExpenseConverter.toProcess(process);
        expense.updateProcess(expenseProcess);

        // 5. 알림 전송
        Members departmentManager = membersService.findDepartmentManager(memberDepartment);
        String message
                = departmentManager.getDepartment().getDepartment()
                +"부서의 지출 내역서가 "
                +expenseProcess.getProcess()
                +" 상태로 처리되었습니다.";
        NotificationData notificationData
                = notificationsService.toNotificationData(
                        departmentManager, loginMember, null,
                null, null, null, message);
        notificationsService.generateEntityAndSendMessage(
                notificationData, NotificationType.UPDATE_EXPENSE_REPORT_STATUS);

        // 6. DTO 변환
        return ExpenseConverter.toUpdateOneForProcessDto(
                expense, loginMember.getMemberName());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDto.ReadOneDto readOne(Long expenseId, Members loginMember){
        // 1. 지출 내역서 존재?
        Expense expense = findById(expenseId);

        // 2. 접근 권한 확인
        Members financeManagerOrCeo = membersService.findByFinanceManagerOrCeo(loginMember.getId());

        if (financeManagerOrCeo == null
                && !expense.getDepartment().equals(loginMember.getDepartment())) {
            throw new ExpenseCustomException(ExpenseExceptionCode.RESTRICTED_ACCESS_EXPENSE);
        }

        return ExpenseConverter.toReadOneDto(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseResponseDto.ReadOneDto> readFiltered(
            ExpenseRequestDto.ReadFilteredDto requestDto,
            Members loginMember, Pageable pageable) {

        // 1-1. Finance Manager 또는 CEO만 전체 부서 접근 가능
        if (requestDto.getDepartment() == null) {
            Members financeManagerOrCeo = membersService.findByFinanceManagerOrCeo(loginMember.getId());
            if (financeManagerOrCeo == null) {
                throw new ExpenseCustomException(ExpenseExceptionCode.RESTRICTED_ACCESS_EXPENSE);
            }
        } else {
            // 1-2. 요청된 부서가 사용자가 속한 부서인지 확인
            MemberDepartment memberDepartment = membersService.findDepartment(requestDto.getDepartment());
            if (!loginMember.getDepartment().equals(memberDepartment)) {
                throw new ExpenseCustomException(ExpenseExceptionCode.RESTRICTED_ACCESS_EXPENSE);
            }
        }
        
        // 2. 날짜 필터링 로직 적용
        DateTimeUtils.validateAndParseDates(requestDto.getCreatedAtStart(), requestDto.getCreatedAtEnd());

        LocalDateTime startDate = requestDto.getCreatedAtStart() != null ? DateTimeUtils.parse(requestDto.getCreatedAtStart()) : null;
        LocalDateTime endDate = requestDto.getCreatedAtEnd() != null ? DateTimeUtils.parse(requestDto.getCreatedAtEnd()) : null;

        Page<Expense> filteredExpensePage;

        if (startDate != null && endDate != null) {
            filteredExpensePage = expenseRepository.findByCreatedAtBetween(startDate, endDate.withHour(23).withMinute(59).withSecond(59), pageable);
        } else if (startDate != null) {
            filteredExpensePage = expenseRepository.findByCreatedAtAfter(startDate, pageable);
        } else if (endDate != null) {
            filteredExpensePage = expenseRepository.findByCreatedAtBefore(endDate.withHour(23).withMinute(59).withSecond(59), pageable);
        } else {
            filteredExpensePage = expenseRepository.findAll(pageable);
        }

        // 3. 필터링된 지출 내역을 DTO로 변환
        return ExpenseConverter.toReadFilteredPageDto(filteredExpensePage);
    }

    @Override
    @Transactional
    public ExpenseResponseDto.UpdateOneDto updateOne(
            Long expenseId, List<MultipartFile> multipartFileList,
            Members loginMember, ExpenseRequestDto.UpdateOneDto requestDto){
        Expense expense = findById(expenseId);

        List<String> fileUrlList
                = expense.getFileList().stream().map(Files::getUrl)
                .collect(Collectors.toList());
        filesService.deleteForExpense(expenseId, fileUrlList);

        if(multipartFileList != null){
            for(MultipartFile multipartFile : multipartFileList){
                filesService.createOneForExpense(
                        multipartFile, expense, loginMember);
            }
        }

        expense.update(
                requestDto.getTitle(), requestDto.getDetails(), requestDto.getMoney());
        expense.updateProcess(ExpenseProcess.PENDING);

        String message
                = loginMember.getMemberName()
                +"님이 비용 지출 내역서를 수정하셨습니다.";

        Members financeManager = membersService.findByFinanceManager();
        NotificationData notificationData = notificationsService.toNotificationData(
                financeManager, loginMember, null, null,
                null, null, message);
        notificationsService.generateEntityAndSendMessage(
                notificationData, NotificationType.UPDATE_EXPENSE_REPORT);

        return ExpenseConverter.toUpdateOneDto(expense);
    }

    @Override
    @Transactional
    public void deleteOne(Long expenseId, Members loginMember) {
        Expense expense = findById(expenseId);
        Members financeManagerOrCeo
                = membersService.findByFinanceManagerOrCeo(loginMember.getId());
        if(financeManagerOrCeo == null
                && !loginMember.getMemberName().equals(expense.getMemberName())){
            throw new ExpenseCustomException(
                    ExpenseExceptionCode.DO_NOT_DELETE_EXPENSE_DEPARTMENT);
        }
        expenseRepository.deleteById(expenseId);
    }

    public Expense findById(Long expenseId){
        return expenseRepository.findById(expenseId).orElseThrow(
                ()-> new ExpenseCustomException(ExpenseExceptionCode.NOT_FOUND_EXPENSE));
    }
}
