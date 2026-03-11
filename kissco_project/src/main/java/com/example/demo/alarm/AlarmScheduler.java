package com.example.demo.alarm;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlarmScheduler {

    private final AlarmService alarmService;

    public AlarmScheduler(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    // 매시간 정각 실행
    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        System.out.println("[ALARM SCHEDULER] 정각 스케줄러 실행됨: " + LocalDateTime.now());
        alarmService.processAllSchedulesAndNotify();
    }
}