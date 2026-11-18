package com.ermapsh.notifier.app.entity;

import com.ermapsh.notifier.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(
        name = "daily_schedules"
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySchedule extends BaseEntity implements Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reminder_id", nullable = false, unique = true)
    private Reminder reminder;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "reminder_time", nullable = false)
    private LocalTime reminderTime;

    @Column(name = "time_zone", nullable = false, length = 64)
    private String timeZone;
}