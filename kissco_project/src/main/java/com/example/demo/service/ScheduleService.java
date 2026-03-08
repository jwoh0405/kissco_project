package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    public List<Schedule> getSchedulesByMember(Long memberNo){

        List<Schedule> schedules = scheduleRepository.findByMemberNo(memberNo);

        LocalDateTime now = LocalDateTime.now();

        List<Schedule> active = new ArrayList<>();
        List<Schedule> past = new ArrayList<>();

        for(Schedule s : schedules){

            if(Boolean.TRUE.equals(s.getIsCompleted()) ||
               (s.getDeadline() != null && s.getDeadline().isBefore(now))){

                past.add(s);

            } else {

                active.add(s);
            }
        }

        Comparator<Schedule> sortRule =
                Comparator.comparing(Schedule::getDeadline)
                          .thenComparing(Schedule::getImportance, Comparator.reverseOrder());

        active.sort(sortRule);
        past.sort(sortRule);

        active.addAll(past);

        return active;
    }
    
    // 전체 조회(알림 스케줄러용)
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
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
    public Schedule getSchedule(Long id, Long memberNo){

        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        if(schedule == null){
            throw new RuntimeException("schedule not found");
        }

        //내 일정인지 확인
        if(!schedule.getMemberNo().equals(memberNo)){
            throw new RuntimeException("권한 없음");
        }

        return schedule;
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
    
    //토글
    public void updateAlert(Long id, Boolean alertEnabled, Long memberNo) {

        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        if(schedule == null){
            throw new RuntimeException("schedule not found");
        }

        //본인 스케줄인지 확인
        if(!schedule.getMemberNo().equals(memberNo)){
            throw new RuntimeException("권한 없음");
        }

        schedule.setAlertEnabled(alertEnabled);

        scheduleRepository.save(schedule);
    }
}