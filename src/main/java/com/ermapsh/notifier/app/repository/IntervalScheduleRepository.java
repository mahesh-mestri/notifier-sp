package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.IntervalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IntervalScheduleRepository extends JpaRepository<IntervalSchedule, UUID> {
    Optional<IntervalSchedule> findByReminderId(UUID id);
}