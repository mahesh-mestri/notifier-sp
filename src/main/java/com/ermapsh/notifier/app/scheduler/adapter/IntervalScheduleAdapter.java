package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.IntervalScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.IntervalSchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import com.ermapsh.notifier.app.repository.IntervalScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntervalScheduleAdapter implements ScheduleAdapter {

    private final IntervalScheduleRepository intervalScheduleRepository;

    @Override
    public Schedule create(Reminder reminder, ReminderScheduleRequest schedule) {
        if (!(schedule instanceof IntervalScheduleRequest request)) {
            throw new IllegalArgumentException("Invalid schedule request for INTERVAL reminder");
        }

        IntervalSchedule intervalSchedule = IntervalSchedule.builder()
                .reminder(reminder)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .intervalValue(request.intervalValue())
                .intervalUnit(request.intervalUnit())
                .windowStartTime(request.windowStartTime())
                .windowEndTime(request.windowEndTime())
                .timeZone(request.timeZone())
                .build();

        return intervalScheduleRepository.save(intervalSchedule);
    }

    @Override
    public List<ReminderOccurrence> arrangeReminderOccurrence(
            Reminder reminder,
            Schedule schedule
    ) {
        if (!(schedule instanceof IntervalSchedule intervalSchedule)) {
            throw new IllegalArgumentException(
                    "Invalid schedule configuration for INTERVAL reminder"
            );
        }

        ZoneId zoneId = ZoneId.of(intervalSchedule.getTimeZone());

        Instant now = Instant.now();
        Instant until = now.plus(30, ChronoUnit.DAYS);

        LocalDate startDate = intervalSchedule.getStartDate();
        LocalDate endDate = intervalSchedule.getEndDate();

        LocalTime windowStartTime = intervalSchedule.getWindowStartTime();
        LocalTime windowEndTime = intervalSchedule.getWindowEndTime();

        int intervalValue = intervalSchedule.getIntervalValue();

        Duration interval = switch (intervalSchedule.getIntervalUnit()) {
            case MINUTES -> Duration.ofMinutes(intervalValue);
            case HOURS -> Duration.ofHours(intervalValue);
        };

        if (windowEndTime.isBefore(windowStartTime)) {
            throw new IllegalArgumentException(
                    "Window end time must not be before window start time"
            );
        }

        LocalDate currentDate = now.atZone(zoneId).toLocalDate();

        if (startDate.isAfter(currentDate)) {
            currentDate = startDate;
        }

        List<ReminderOccurrence> occurrences = new ArrayList<>();

        while (true) {

            // Respect configured end date.
            if (endDate != null && currentDate.isAfter(endDate)) {
                break;
            }

            Instant windowStart = currentDate
                    .atTime(windowStartTime)
                    .atZone(zoneId)
                    .toInstant();

            Instant windowEnd = currentDate
                    .atTime(windowEndTime)
                    .atZone(zoneId)
                    .toInstant();

            // Stop when the generation window is exceeded.
            if (windowStart.isAfter(until)) {
                break;
            }

            Instant scheduledAt = windowStart;

            while (!scheduledAt.isAfter(windowEnd)
                    && !scheduledAt.isAfter(until)) {

                if (scheduledAt.isAfter(now)) {

                    occurrences.add(
                            ReminderOccurrence.builder()
                                    .reminder(reminder)
                                    .scheduledAt(scheduledAt)
                                    .status(OccurrenceStatus.PENDING)
                                    .build()
                    );

                    log.info(
                            "Generated interval occurrence: reminderId={}, scheduledAt={}",
                            reminder.getId(),
                            scheduledAt
                    );
                }

                scheduledAt = scheduledAt.plus(interval);
            }

            currentDate = currentDate.plusDays(1);
        }

        return occurrences;
    }

    @Override
    public Schedule findByReminderId(Reminder reminder) {
        return intervalScheduleRepository
                .findByReminderId(reminder.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Interval schedule not found for reminder: "
                                + reminder.getId()
                ));
    }


}