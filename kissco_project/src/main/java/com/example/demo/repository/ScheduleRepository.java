package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	List<Schedule> findByMemberNo(Long memberNo);
}