package com.ermapsh.notifier.app.dto.req;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record DailyScheduleRequest(
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull LocalTime reminderTime,
        @NotNull String timeZone
) implements ReminderScheduleRequest {}
