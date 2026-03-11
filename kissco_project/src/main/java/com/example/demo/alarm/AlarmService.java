package com.example.demo.alarm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.model.Schedule;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MailService;
import com.example.demo.service.ScheduleService;

@Service
public class AlarmService {

    private final ScheduleService scheduleService;
    private final MailService mailService;
    private final MemberRepository memberRepository;

    // 서버 실행 중 메모리 중복 방지
    private final Set<String> sentKeys = ConcurrentHashMap.newKeySet();

    public AlarmService(ScheduleService scheduleService,
                        MailService mailService,
                        MemberRepository memberRepository) {
        this.scheduleService = scheduleService;
        this.mailService = mailService;
        this.memberRepository = memberRepository;
    }

    /**
     * DB에서 일정 전체 조회해서
     * 매시간 정각 기준 "24시간 뒤 같은 시간대" 일정만 메일 발송
     */
    public void processAllSchedulesAndNotify() {

        List<Schedule> schedules = scheduleService.getAllSchedules();
        LocalDateTime now = LocalDateTime.now();

        for (Schedule s : schedules) {
            if (s == null) continue;

            // 1) 알림 ON
            if (Boolean.FALSE.equals(s.getAlertEnabled())) continue;

            // 2) 완료 일정 제외
            if (Boolean.TRUE.equals(s.getIsCompleted())) continue;

            // 3) deadline 체크
            LocalDateTime deadline = s.getDeadline();
            if (deadline == null) continue;

            LocalDateTime currentHour = now.withMinute(0).withSecond(0).withNano(0);
            LocalDateTime start = currentHour.plusHours(24);
            LocalDateTime end = start.plusHours(1);
            
            // 내일 같은 시간대 일정만 알림
            if (deadline.isBefore(start) || !deadline.isBefore(end)) continue;

            // 4) 중복 방지(메모리)
            String key = (s.getId() != null)
                    ? "scheduleId:" + s.getId()
                    : "member:" + s.getMemberNo() + "|deadline:" + deadline;

            if (!sentKeys.add(key)) {
                System.out.println("[ALARM] 이미 발송 처리됨(메모리): " + key);
                continue;
            }

            // 5) 회원 이메일 조회
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

            // 6) 메일 내용 작성
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

            // 7) 메일 발송
            try {
                mailService.sendMail(email, subject, body);
                System.out.println("[ALARM] 메일 발송 완료: " + email);
            } catch (Exception e) {
                System.out.println("[ALARM] 메일 발송 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}