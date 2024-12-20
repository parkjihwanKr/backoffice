package com.example.backoffice.domain.vacation.service;

import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.domain.vacation.entity.Vacations;
import com.example.backoffice.domain.vacation.exception.VacationsCustomException;
import com.example.backoffice.domain.vacation.exception.VacationsExceptionCode;
import com.example.backoffice.domain.vacation.repository.VacationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationsServiceImplV1 implements VacationsServiceV1 {

    private final VacationsRepository vacationsRepository;
    @Override
    @Transactional
    public Vacations save(Vacations vacation){
        return vacationsRepository.save(vacation);
    }

    @Override
    @Transactional(readOnly = true)
    public Vacations findById(Long vacationId) {
        return vacationsRepository.findById(vacationId).orElseThrow(
                () -> new VacationsCustomException(VacationsExceptionCode.NOT_FOUND_VACATIONS));
    }

    @Override
    @Transactional
    public List<Vacations> findVacationsOnMonth(LocalDateTime startDate, LocalDateTime endDate){
        return vacationsRepository.findVacationsOnMonth(startDate, endDate);
    }

    @Override
    @Transactional
    public List<Vacations> findAcceptedVacationByMemberIdAndDateRange(
            Long memberId, Boolean isAccepted,
            LocalDateTime startDate, LocalDateTime endDate){
        return vacationsRepository.findAcceptedVacationByMemberIdAndDateRange(
                memberId, true, startDate, endDate);
    }

    @Override
    @Transactional
    public List<Vacations> findAllByMemberIdAndStartDate(Long memberId, LocalDateTime startDate){
        return vacationsRepository.findAllByMemberIdAndStartDate(memberId, startDate);
    }

    @Override
    @Transactional
    public void deleteById(Long vacationId){
        vacationsRepository.deleteById(vacationId);
    }

    @Override
    @Transactional
    public Long countVacationingMembers(LocalDateTime customStartDate){
        return vacationsRepository.countVacationingMembers(customStartDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vacations> findAllByEndDateBefore(LocalDateTime now){
        return vacationsRepository.findAllByEndDateBefore(now);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vacations> findAllByStartDate(LocalDateTime now){
        return vacationsRepository.findAllByStartDate(now);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsVacationForMemberInDateRange(
            Long vacationId, Long memberId, LocalDateTime startDate, LocalDateTime endDate){
        return vacationsRepository.existsVacationForMemberInDateRange(
                vacationId, memberId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vacations> findFilteredVacationsOnMonth(
            LocalDateTime startDate, LocalDateTime endDate,
            Boolean isAccepted, Boolean urgent, MemberDepartment memberDepartment){
        return vacationsRepository.findFilteredVacationsOnMonth(
                startDate, endDate, isAccepted, urgent, memberDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vacations> findVacationsBetweenOrderByCreatedAtDesc(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate){
        return vacationsRepository.findVacationsBetweenOrderByCreatedAtDesc(
                memberId, startDate, endDate);
    }
}
