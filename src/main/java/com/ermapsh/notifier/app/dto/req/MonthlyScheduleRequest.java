package com.ermapsh.notifier.app.dto.req;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record MonthlyScheduleRequest(
        @NotNull LocalDate startDate,
        LocalDate endDate,
        @NotNull LocalTime reminderTime,

        @NotNull
        @Min(1)
        @Max(31)
        Integer dayOfMonth,

        @NotBlank String timeZone
) implements ReminderScheduleRequest {}
