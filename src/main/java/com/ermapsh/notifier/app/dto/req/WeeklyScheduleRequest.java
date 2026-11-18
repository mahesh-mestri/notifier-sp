package com.ermapsh.notifier.app.dto.req;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record WeeklyScheduleRequest(
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull LocalTime reminderTime,

        @NotEmpty
        Set<@Min(0) @Max(6) Integer> weekdays,

        @NotBlank String timeZone
) implements ReminderScheduleRequest {}
