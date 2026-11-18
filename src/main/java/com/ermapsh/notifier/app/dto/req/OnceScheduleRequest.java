package com.ermapsh.notifier.app.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record OnceScheduleRequest(
        @NotNull LocalDate reminderDate,
        @NotNull LocalTime reminderTime,
        @NotBlank String timeZone
) implements ReminderScheduleRequest {}
