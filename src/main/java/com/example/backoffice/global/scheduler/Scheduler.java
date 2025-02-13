package com.example.backoffice.global.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final DailyScheduler dailyScheduler;
    private final MonthlyScheduler monthlyScheduler;
    private final YearlyScheduler yearlyScheduler;

    @Scheduled(cron = "0 0 0 * * *")
    public void executeDailyTask() {
        dailyScheduler.execute();
    }

    // 매월 1일 00:00 실행
    @Scheduled(cron = "0 0 0 1 * *")
    public void executeMonthlyTask() {
        monthlyScheduler.execute();
    }

    // 매년 1월 1일 00:00 실행
    @Scheduled(cron = "0 0 0 1 1 *")
    public void executeYearlyTask() {
        yearlyScheduler.execute();
    }
}