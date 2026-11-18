package com.ermapsh.notifier.app.entity;

import com.ermapsh.notifier.app.enums.ReminderCategory;
import com.ermapsh.notifier.app.enums.ReminderScheduleType;
import com.ermapsh.notifier.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(
        name = "reminders",
        indexes = {
                @Index(name = "idx_reminder_user_id", columnList = "user_id"),
                @Index(name = "idx_reminder_active", columnList = "active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_id", nullable = false, length = 30)
    private ReminderCategory categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false, length = 20)
    private ReminderScheduleType scheduleType;

    @Column(name = "notification_enabled", nullable = false)
    @Builder.Default
    private boolean notificationEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}