package com.ermapsh.notifier.app.dto.req;


import com.ermapsh.notifier.app.enums.IntervalUnit;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record IntervalScheduleRequest(
        @NotNull LocalDate startDate,
        LocalDate endDate,

        @NotNull
        @Min(1)
        Integer intervalValue,

        @NotNull
        IntervalUnit intervalUnit,

        @NotNull LocalTime windowStartTime,
        @NotNull LocalTime windowEndTime,

        @NotBlank String timeZone
) implements ReminderScheduleRequest {}
