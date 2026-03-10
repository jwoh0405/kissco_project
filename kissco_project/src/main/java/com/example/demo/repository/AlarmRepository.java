package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Schedule;

public interface AlarmRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByAlertEnabledAndIsCompleted(Integer alertEnabled, Integer isCompleted);
}