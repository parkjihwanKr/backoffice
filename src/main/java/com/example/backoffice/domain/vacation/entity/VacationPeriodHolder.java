package com.example.backoffice.domain.vacation.entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class VacationPeriodHolder {

    // VacationPeriod를 가져오는 메서드
    private VacationPeriod vacationPeriod;  // VacationPeriod를 저장하는 변수

    // 생성자 주입을 통한 VacationPeriod 설정
    @Builder
    public VacationPeriodHolder(VacationPeriod vacationPeriod) {
        this.vacationPeriod = vacationPeriod;
    }
}
