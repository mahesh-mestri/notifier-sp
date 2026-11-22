
package com.ermapsh.notifier.app.entity;

import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "reminder_occurrences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reminder_scheduled_at",
                        columnNames = {"reminder_id", "scheduled_at"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_occurrence_status_scheduled_at",
                        columnList = "status, scheduled_at"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderOccurrence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reminder_id", nullable = false)
    private Reminder reminder;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OccurrenceStatus status = OccurrenceStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();

        if (status == null) {
            status = OccurrenceStatus.PENDING;
        }
    }
}
