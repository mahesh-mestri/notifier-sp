package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.MonthlyScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.MonthlySchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import com.ermapsh.notifier.app.repository.MonthlyScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonthlyScheduleAdapter
        implements ScheduleAdapter {

    private final MonthlyScheduleRepository monthlyScheduleRepository;

    @Override
    public Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule
    ) {
        if (!(schedule instanceof MonthlyScheduleRequest request)) {
            throw new IllegalArgumentException(
                    "Invalid schedule request for MONTHLY reminder"
            );
        }

        MonthlySchedule monthlySchedule = MonthlySchedule.builder()
                .reminder(reminder)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reminderTime(request.reminderTime())
                .dayOfMonth(request.dayOfMonth())
                .timeZone(request.timeZone())
                .build();

        return monthlyScheduleRepository.save(monthlySchedule);
    }

    @Override
    public List<ReminderOccurrence> arrangeReminderOccurrence(
            Reminder reminder,
            Schedule schedule
    ) {
        if (!(schedule instanceof MonthlySchedule monthlySchedule)) {
            throw new IllegalArgumentException(
                    "Invalid schedule configuration for MONTHLY reminder"
            );
        }

        ZoneId zoneId = ZoneId.of(monthlySchedule.getTimeZone());

        Instant now = Instant.now();
        Instant until = now.plus(30, ChronoUnit.DAYS);

        LocalDate startDate = monthlySchedule.getStartDate();
        LocalDate endDate = monthlySchedule.getEndDate();

        int dayOfMonth = monthlySchedule.getDayOfMonth();

        List<ReminderOccurrence> occurrences = new ArrayList<>();

        YearMonth currentMonth = YearMonth.from(startDate);

        while (true) {

            // Calculate the valid day for this month.
            int actualDay = Math.min(
                    dayOfMonth,
                    currentMonth.lengthOfMonth()
            );

            LocalDate occurrenceDate = currentMonth.atDay(actualDay);

            // Respect start date.
            if (occurrenceDate.isBefore(startDate)) {
                currentMonth = currentMonth.plusMonths(1);
                continue;
            }

            // Respect end date.
            if (endDate != null && occurrenceDate.isAfter(endDate)) {
                break;
            }

            Instant scheduledAt = occurrenceDate
                    .atTime(monthlySchedule.getReminderTime())
                    .atZone(zoneId)
                    .toInstant();

            // Stop after the generation window.
            if (scheduledAt.isAfter(until)) {
                break;
            }

            // Ignore past occurrences.
            if (scheduledAt.isAfter(now)) {

                occurrences.add(
                        ReminderOccurrence.builder()
                                .reminder(reminder)
                                .scheduledAt(scheduledAt)
                                .status(OccurrenceStatus.PENDING)
                                .build()
                );

                log.info(
                        "Generated monthly occurrence: reminderId={}, scheduledAt={}",
                        reminder.getId(),
                        scheduledAt
                );
            }

            currentMonth = currentMonth.plusMonths(1);
        }

        return occurrences;
    }


    @Override
    public Schedule findByReminderId(Reminder reminder) {
        return monthlyScheduleRepository
                .findByReminderId(reminder.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Monthly schedule not found for reminder: "
                                + reminder.getId()
                ));
    }
}