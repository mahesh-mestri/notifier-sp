package com.ermapsh.notifier.app.entity;

import com.ermapsh.notifier.app.enums.IntervalUnit;
import com.ermapsh.notifier.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(
        name = "interval_schedules"
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntervalSchedule extends BaseEntity implements Schedule{

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

    @Column(name = "interval_value", nullable = false)
    private Integer intervalValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "interval_unit", nullable = false, length = 20)
    private IntervalUnit intervalUnit;

    @Column(name = "window_start_time", nullable = false)
    private LocalTime windowStartTime;

    @Column(name = "window_end_time", nullable = false)
    private LocalTime windowEndTime;

    @Column(name = "time_zone", nullable = false, length = 64)
    private String timeZone;
}