package com.example.demo.alarm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.demo.model.Schedule;
import com.example.demo.service.MailService;
import com.example.demo.service.ScheduleService;

@Service
public class AlarmService {

    private final ScheduleService scheduleService;
    private final MailService mailService;

    // DB 업데이트 실패 시 스팸 방지
    private final Set<String> sentKeys = ConcurrentHashMap.newKeySet();

    public AlarmService(ScheduleService scheduleService, MailService mailService) {
        this.scheduleService = scheduleService;
        this.mailService = mailService;
    }

    /**
     * DB에서 일정 전체 조회해서 알림 조건 맞는 것만 메일 발송
     * - 테스트 단계: 받는 사람 이메일을 고정(testReceiverEmail)
     * - 실서비스: memberNo로 회원 이메일 조회해서 보내도록 바꾸면 됨
     */
    public void processAllSchedulesAndNotify(String testReceiverEmail) {

        List<Schedule> schedules = scheduleService.getAllSchedules();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("[ALARM] schedules size=" + schedules.size() + ", now=" + now);

        for (Schedule s : schedules) {
            if (s == null) continue;

            // --- 디버깅 출력(확인용) ---
            System.out.println("ID: " + s.getId());
            System.out.println("TITLE: " + s.getTitle());
            System.out.println("ALERT_ENABLED: " + s.getAlertEnabled());
            System.out.println("IS_NOTIFIED: " + s.getIsNotified());
            System.out.println("IS_COMPLETED: " + s.getIsCompleted());
            System.out.println("DEADLINE: " + s.getDeadline());
            System.out.println("MEMBER_NO: " + s.getMemberNo());
            // --------------------------------

            // 1) 알림 ON
            if (!"Y".equals(s.getAlertEnabled())) continue;

            // 2) 이미 보냄
            if ("Y".equals(s.getIsNotified())) continue;

            // 3) 완료 일정 제외
            if ("Y".equals(s.getIsCompleted())) continue;

            // 4) deadline 체크
            LocalDateTime deadline = s.getDeadline();
            if (deadline == null) continue;

            long secondsLeft = Duration.between(now, deadline).getSeconds();
            if (secondsLeft < 0) continue;

            // 10분 전 판정
            if (!(secondsLeft >= 600 && secondsLeft <= 609)) continue;

            // 중복 방지(메모리)
            String key = (s.getId() != null) ? "scheduleId:" + s.getId() : "member:" + s.getMemberNo() + "|deadline:" + deadline;
            if (!sentKeys.add(key)) {
                System.out.println("[ALARM] 이미 발송 처리됨(메모리): " + key);
                continue;
            }

            // 메일 발송
            String subject = "[알림] " + s.getTitle();
            String body =
                    "일정 알림입니다.\n\n"
                    + "제목: " + s.getTitle() + "\n"
                    + "내용: " + (s.getContent() == null ? "" : s.getContent()) + "\n"
                    + "시간: " + deadline + "\n"
                    + "이 일정은 10분 후 시작됩니다.\n";

            mailService.sendMail(testReceiverEmail, subject, body);
            System.out.println("[ALARM] ✅ 메일 발송 완료: " + key);

            // DB에 보냄 처리 (IS_NOTIFIED = 'Y')
//            try {
//                s.setIsNotified("Y");
//                scheduleService.updateSchedule(s.getId(), s, s.getMemberNo()); // 권한체크 때문에 memberNo 넣음
//                System.out.println("[ALARM] ✅ DB 업데이트 완료(IS_NOTIFIED=Y): id=" + s.getId());
//            } catch (Exception e) {
//                System.out.println("[ALARM] ⚠ DB 업데이트 실패(그래도 메모리로 중복 방지됨): " + e.getMessage());
//            }
        }
    }
}