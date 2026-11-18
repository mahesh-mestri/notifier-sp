package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
}