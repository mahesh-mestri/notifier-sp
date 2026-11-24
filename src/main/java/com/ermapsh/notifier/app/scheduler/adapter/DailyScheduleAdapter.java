
package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.DailyScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.DailySchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import com.ermapsh.notifier.app.repository.DailyScheduleRepository;
import com.ermapsh.notifier.app.scheduler.OccurrenceCalculator;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyScheduleAdapter implements ScheduleAdapter {

    private final DailyScheduleRepository dailyScheduleRepository;

    @Override
    public Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule
    ) {
        if (!(schedule instanceof DailyScheduleRequest request)) {
            throw new IllegalArgumentException(
                    "Invalid schedule request for DAILY reminder"
            );
        }

        DailySchedule dailySchedule = DailySchedule.builder()
                .reminder(reminder)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reminderTime(request.reminderTime())
                .timeZone(request.timeZone())
                .build();

        return dailyScheduleRepository.save(dailySchedule);
    }

    @Override
    public List<ReminderOccurrence> arrangeReminderOccurrence(
            Reminder reminder,
            Schedule schedule
    ) {
        if (!(schedule instanceof DailySchedule dailySchedule)) {
            throw new IllegalArgumentException(
                    "Invalid schedule configuration for DAILY reminder"
            );
        }

        ZoneId zoneId = ZoneId.of(dailySchedule.getTimeZone());

        Instant now = Instant.now();
        Instant until = now.plus(30, ChronoUnit.DAYS);

        LocalDate startDate = dailySchedule.getStartDate();
        LocalDate endDate = dailySchedule.getEndDate();

        // Start from today if the configured start date is in the past.
        LocalDate currentDate = now.atZone(zoneId).toLocalDate();

        if (startDate.isAfter(currentDate)) {
            currentDate = startDate;
        }

        List<ReminderOccurrence> occurrences = new ArrayList<>();

        while (true) {

            // Respect the configured end date (inclusive).
            if (endDate != null && currentDate.isAfter(endDate)) {
                break;
            }

            Instant scheduledAt = currentDate
                    .atTime(dailySchedule.getReminderTime())
                    .atZone(zoneId)
                    .toInstant();

            // Ignore past occurrences.
            if (scheduledAt.isAfter(now)) {

                // Stop when we exceed the 30-day generation window.
                if (scheduledAt.isAfter(until)) {
                    break;
                }

                occurrences.add(
                        ReminderOccurrence.builder()
                                .reminder(reminder)
                                .scheduledAt(scheduledAt)
                                .status(OccurrenceStatus.PENDING)
                                .build()
                );
            }

            currentDate = currentDate.plusDays(1);
        }

        return occurrences;
    }


    @Override
    public Schedule findByReminderId(Reminder reminder) {
        return dailyScheduleRepository
                .findByReminderId(reminder.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Daily schedule not found for reminder: "
                                + reminder.getId()
                ));
    }
}
