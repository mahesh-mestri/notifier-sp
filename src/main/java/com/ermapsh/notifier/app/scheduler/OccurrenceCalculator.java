
package com.ermapsh.notifier.app.scheduler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

public final class OccurrenceCalculator {

    private OccurrenceCalculator() {
        // Utility class
    }

    public static Optional<Instant> daily(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime reminderTime,
            String timeZone,
            Instant after
    ) {
        ZoneId zoneId = ZoneId.of(timeZone);

        LocalDate date = after.atZone(zoneId).toLocalDate();

        if (date.isBefore(startDate)) {
            date = startDate;
        }

        while (endDate == null || !date.isAfter(endDate)) {

            Instant occurrence = date
                    .atTime(reminderTime)
                    .atZone(zoneId)
                    .toInstant();

            if (occurrence.isAfter(after)) {
                return Optional.of(occurrence);
            }

            date = date.plusDays(1);
        }

        return Optional.empty();
    }
}
