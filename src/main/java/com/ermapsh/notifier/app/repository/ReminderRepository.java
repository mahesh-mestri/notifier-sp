package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends JpaRepository<Reminder, UUID> {

    @Query("""
            SELECT r FROM Reminder r
            WHERE r.active = true
            AND NOT EXISTS (
                SELECT o.id FROM ReminderOccurrence o
                WHERE o.reminder = r
            )
            """)
    List<Reminder> findActiveRemindersWithoutOccurrences();

    // Duplicate reminder validation
    boolean existsByUserIdAndTitleIgnoreCaseAndActiveTrue(
            UUID userId,
            String title
    );
}