package com.example.demo.alarm;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlarmScheduler {

    private final AlarmService alarmService;

    public AlarmScheduler(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    // 1분마다
    @Scheduled(cron = "0 * * * * *")
    public void run() {
        alarmService.processAllSchedulesAndNotify();
    }
}