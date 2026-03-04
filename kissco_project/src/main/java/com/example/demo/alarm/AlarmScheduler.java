package com.example.demo.alarm;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlarmScheduler {

    private final AlarmService alarmService;

    public AlarmScheduler(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    // 1분마다 테스트
    @Scheduled(cron = "0 * * * * *")
    public void run() {
        String testReceiverEmail = "jwoh45@gmail.com"; // 일단 너한테만 오게(내일 테스트용)
        alarmService.processAllSchedulesAndNotify(testReceiverEmail);
    }
}