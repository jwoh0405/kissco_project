package com.example.demo.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component		// 스프링이 관리
public class SampleScheduler {
    @Scheduled(fixedRate = 10000)		// 10초마다 실행
    public void ping() {
        System.out.println("[SCHEDULER] running...");
    }
}
