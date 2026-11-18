package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.DailySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DailyScheduleRepository extends JpaRepository<DailySchedule, UUID> {
}