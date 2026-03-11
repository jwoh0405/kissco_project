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
import com.example.demo.repository.MemberRepository;
import com.example.demo.model.Member;
import java.time.format.DateTimeFormatter;

@Service
public class AlarmService {

    private final ScheduleService scheduleService;
    private final MailService mailService;
    private final MemberRepository memberRepository;

    // DB 업데이트 실패 시 스팸 방지
    private final Set<String> sentKeys = ConcurrentHashMap.newKeySet();

    public AlarmService(ScheduleService scheduleService, MailService mailService, MemberRepository memberRepository) {
        this.scheduleService = scheduleService;
        this.mailService = mailService;
        this.memberRepository = memberRepository;
    }

    /**
     * DB에서 일정 전체 조회해서 알림 조건 맞는 것만 메일 발송
     */
    public void processAllSchedulesAndNotify() {

        List<Schedule> schedules = scheduleService.getAllSchedules();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("[ALARM] schedules size=" + schedules.size() + ", now=" + now);

        for (Schedule s : schedules) {
            if (s == null) continue;

            System.out.println("ID: " + s.getId());
            System.out.println("TITLE: " + s.getTitle());
            System.out.println("ALERT_ENABLED: " + s.getAlertEnabled());
            System.out.println("IS_NOTIFIED: " + s.getIsNotified());
            System.out.println("IS_COMPLETED: " + s.getIsCompleted());
            System.out.println("DEADLINE: " + s.getDeadline());
            System.out.println("MEMBER_NO: " + s.getMemberNo());

            // 1) 알림 ON
            if (Boolean.FALSE.equals(s.getAlertEnabled())) continue;

            // 2) 이미 보냄
            if (Boolean.TRUE.equals(s.getIsNotified())) continue;

            // 3) 완료 일정 제외
            if (Boolean.TRUE.equals(s.getIsCompleted())) continue;

            // 4) deadline 체크
            LocalDateTime deadline = s.getDeadline();
            if (deadline == null) continue;

            long secondsLeft = Duration.between(now, deadline).getSeconds();
            if (secondsLeft < 0) continue;

            // 24시간 전 ~ 25시간 전 사이의 일정만 대상으로 함
            if (!(secondsLeft > 86400 && secondsLeft <= 90000)) continue;

            // 중복 방지(메모리)
            String key = (s.getId() != null)
                    ? "scheduleId:" + s.getId()
                    : "member:" + s.getMemberNo() + "|deadline:" + deadline;

            if (!sentKeys.add(key)) {
                System.out.println("[ALARM] 이미 발송 처리됨(메모리): " + key);
                continue;
            }

            // 회원 이메일 조회
            Member member = memberRepository.findById(s.getMemberNo()).orElse(null);
            if (member == null) {
                System.out.println("[ALARM] 회원 정보 없음: memberNo=" + s.getMemberNo());
                continue;
            }

            String email = member.getEmail();
            if (email == null || email.isBlank()) {
                System.out.println("[ALARM] 이메일 없음: memberNo=" + s.getMemberNo());
                continue;
            }

            // 메일 발송
            deadline = s.getDeadline();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

            String formattedDeadline = deadline.format(formatter);

            String subject = "[일정 알림] " + s.getTitle() + " 일정이 곧 시작됩니다";

            String body =
                    "안녕하세요.\n\n"
                    + "등록하신 일정에 대한 알림입니다.\n\n"
                    + "────────────────────────\n"
                    + "📌 일정 제목 : " + s.getTitle() + "\n"
                    + "📝 일정 내용 : " + (s.getContent() == null ? "없음" : s.getContent()) + "\n"
                    + "⏰ 일정 시간 : " + formattedDeadline + "\n"
                    + "────────────────────────\n\n"
                    + "해당 일정은 24시간 후 시작됩니다.\n"
                    + "미리 준비해 주세요.\n\n"
                    + "감사합니다.\n"
                    + "일정 관리 시스템";

            mailService.sendMail(email, subject, body);
            System.out.println("[ALARM] 메일 발송 완료: " + email);

            // DB에 보냄 처리
            try {
                s.setIsNotified(true);
                scheduleService.updateSchedule(s.getId(), s, s.getMemberNo());
                System.out.println("[ALARM] DB 업데이트 완료(IS_NOTIFIED=Y): id=" + s.getId());
            } catch (Exception e) {
                System.out.println("[ALARM] DB 업데이트 실패(그래도 메모리로 중복 방지됨): " + e.getMessage());
            }
        }
    }
}