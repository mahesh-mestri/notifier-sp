package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.OnceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OnceScheduleRepository extends JpaRepository<OnceSchedule, UUID> {
}