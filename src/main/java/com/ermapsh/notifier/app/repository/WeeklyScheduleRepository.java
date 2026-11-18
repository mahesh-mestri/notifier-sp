package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WeeklyScheduleRepository extends JpaRepository<WeeklySchedule, UUID> {
}