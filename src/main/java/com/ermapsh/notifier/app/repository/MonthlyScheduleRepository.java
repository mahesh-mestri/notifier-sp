package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.MonthlySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonthlyScheduleRepository extends JpaRepository<MonthlySchedule, UUID> {
}