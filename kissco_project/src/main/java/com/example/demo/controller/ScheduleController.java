package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Schedule;
import com.example.demo.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/test")
    public String test() {
        return "schedule controller working";
    }

    //전체 조회
    @GetMapping
    public List<Schedule> getAll(HttpSession session) {

        Long memberNo = (Long) session.getAttribute("loginUser");

        if (memberNo == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "login required");
        }

        return scheduleService.getSchedulesByMember(memberNo);
    }

    //등록
    @PostMapping
    public String create(@RequestBody Schedule schedule,
                         HttpSession session) {

        Long memberNo = (Long) session.getAttribute("loginUser");

        if (memberNo == null) {
            return "login required";
        }

        schedule.setMemberNo(memberNo);

        scheduleService.createSchedule(schedule);

        return "success";
    }

    //삭제
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session) {

        Long memberNo = (Long) session.getAttribute("loginUser");

        if (memberNo == null) {
            return "login required";
        }

        scheduleService.deleteSchedule(id, memberNo);

        return "success";
    }

    //단건 조회
    @GetMapping("/{id}")
    public Schedule getOne(@PathVariable Long id) {
        return scheduleService.getSchedule(id);
    }

    //수정
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestBody Schedule schedule,
                         HttpSession session) {

        Long memberNo = (Long) session.getAttribute("loginUser");

        if (memberNo == null) {
            return "login required";
        }

        scheduleService.updateSchedule(id, schedule, memberNo);

        return "success";
    }
}