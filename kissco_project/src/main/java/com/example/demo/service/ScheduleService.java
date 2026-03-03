package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Schedule;
import com.example.demo.repository.ScheduleRepository;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    //전체 조회
    public List<Schedule> getSchedulesByMember(Long memberNo) {
        return scheduleRepository.findByMemberNo(memberNo);
    }

    //등록
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    //삭제
    public void deleteSchedule(Long id, Long memberNo) {

        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        if (schedule == null) {
            throw new RuntimeException("일정 없음");
        }

        if (!schedule.getMemberNo().equals(memberNo)) {
            throw new RuntimeException("권한 없음");
        }

        scheduleRepository.deleteById(id);
    }

    //단건 조회
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    //수정
    public Schedule updateSchedule(Long id, Schedule updated, Long memberNo) {

        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        if (schedule == null) {
            return null;
        }

        //내 일정인지 확인
        if (!schedule.getMemberNo().equals(memberNo)) {
            throw new RuntimeException("권한 없음");
        }

        schedule.setTitle(updated.getTitle());
        schedule.setContent(updated.getContent());
        schedule.setDeadline(updated.getDeadline());
        schedule.setImportance(updated.getImportance());
        schedule.setAlertEnabled(updated.getAlertEnabled());
        schedule.setIsCompleted(updated.getIsCompleted());
        schedule.setIsNotified(updated.getIsNotified());

        return scheduleRepository.save(schedule);
    }
}