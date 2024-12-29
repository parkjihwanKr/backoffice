package com.example.backoffice.domain.finance.service;

import com.example.backoffice.domain.finance.converter.FinanceConverter;
import com.example.backoffice.domain.finance.dto.FinanceRequestDto;
import com.example.backoffice.domain.finance.dto.FinanceResponseDto;
import com.example.backoffice.domain.finance.entity.Finance;
import com.example.backoffice.domain.finance.exception.FinanceCustomException;
import com.example.backoffice.domain.finance.exception.FinanceExceptionCode;
import com.example.backoffice.domain.finance.repository.FinanceRepository;
import com.example.backoffice.domain.member.entity.Members;
import com.example.backoffice.domain.member.service.MembersServiceV1;
import com.example.backoffice.domain.openBank.config.OpenBankProperties;
import com.example.backoffice.domain.openBank.dto.TokenRequestDto;
import com.example.backoffice.domain.openBank.service.OpenBankApiServiceV1;
import com.example.backoffice.domain.openBank.token.OpenBankRequestToken;
import com.example.backoffice.domain.openBank.token.OpenBankResponseToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FinanceServiceImplV1 implements FinanceServiceV1{

    private final OpenBankProperties openBankProperties;
    private final MembersServiceV1 membersService;
    private final OpenBankApiServiceV1 openBankApiService;
    private final FinanceRepository financeRepository;

    //  Authorization : Bearer_
    //  Long fintech_use_num : 핀테크이용번호,
    //  Long bank_tran_id : 은행거래고유번호,
    //  Long tran_dtime : 요청일시

    @PostConstruct
    public void initializedEntity(){
        Finance finance = financeRepository.findById(1L).orElse(null);
        if(finance == null){
            Finance initialFinance = FinanceConverter.toEntity(
                    BigDecimal.ONE, "원", null, "초기",
                    "", "", null,
                    null);
            financeRepository.save(initialFinance);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FinanceResponseDto.ReadOneDto readOne(
            Long financeId, Members loginMember) {
        validatedAccessMember(loginMember.getId());

        Finance finance = findById(financeId);

        return FinanceConverter.toReadOneDto(finance);
    }

    @Override
    @Transactional
    public FinanceResponseDto.CreateDepartmentBudgetDto createDepartmentBudget(
            Members loginMember, FinanceRequestDto.CreateDepartmentBudgetDto requestDto){
        // 1. 부서 예산을 할당할 수 있는 사람인지?
        validatedAccessMember(loginMember.getId());

        // 2. 금융 결제원으로부터 토큰 발급
        TokenRequestDto tokenDto = FinanceConverter.toTokenRequestDto();
        OpenBankResponseToken openBankResponseToken
                = requestFinanceToken(tokenDto);

        // 3. 금융 결제원으로 받은 토큰과 일부 정보를 db에 저장
        Finance finance = FinanceConverter.toEntity(
                requestDto.getAllocatedBudget(), null, null,
                requestDto.getDescription(), openBankResponseToken.getRsp_code(),
                openBankResponseToken.getRsp_message(), openBankResponseToken.getExpires_in(),
                null);
        financeRepository.save(finance);

        // 4. 리턴
        return FinanceConverter.toCreateDepartmentBudgetDto(finance);
    }

    public Finance findById(Long financeId){
        return financeRepository.findById(financeId).orElseThrow(
                ()-> new FinanceCustomException(FinanceExceptionCode.NOT_FOUND_FINANCE));
    }

    private void validatedAccessMember(Long loginMemberId) {
        Members financeMangerOrCeo
                = membersService.findByFinanceManagerOrCeo(loginMemberId);
        if (financeMangerOrCeo == null) {
            throw new FinanceCustomException(FinanceExceptionCode.RESTRICTED_ACCESS);
        }
    }

    private OpenBankResponseToken requestFinanceToken(TokenRequestDto tokenRequestDto){
        return openBankApiService.requestToken(
                FinanceConverter.toOpenBankRequestToken(
                        tokenRequestDto, openBankProperties));
    }
}
