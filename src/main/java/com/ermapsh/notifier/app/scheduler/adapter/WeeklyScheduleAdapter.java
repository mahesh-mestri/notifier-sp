package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.dto.req.WeeklyScheduleRequest;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.entity.WeeklySchedule;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import com.ermapsh.notifier.app.repository.WeeklyScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class WeeklyScheduleAdapter implements ScheduleAdapter {

    private final WeeklyScheduleRepository weeklyScheduleRepository;

    @Override
    public Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule
    ) {
        if (!(schedule instanceof WeeklyScheduleRequest request)) {
            throw new IllegalArgumentException(
                    "Invalid schedule request for WEEKLY reminder"
            );
        }

        WeeklySchedule weeklySchedule = WeeklySchedule.builder()
                .reminder(reminder)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reminderTime(request.reminderTime())
                .weekdays(request.weekdays())
                .timeZone(request.timeZone())
                .build();

        return weeklyScheduleRepository.save(weeklySchedule);
    }


    @Override
    public List<ReminderOccurrence> arrangeReminderOccurrence(
            Reminder reminder,
            Schedule schedule
    ) {
        if (!(schedule instanceof WeeklySchedule weeklySchedule)) {
            throw new IllegalArgumentException(
                    "Invalid schedule type for WEEKLY reminder"
            );
        }

        ZoneId zoneId = ZoneId.of(weeklySchedule.getTimeZone());

        Instant now = Instant.now();
        Instant until = now.plus(30, ChronoUnit.DAYS);

        LocalDate currentDate = LocalDate.now(zoneId);

        if (currentDate.isBefore(weeklySchedule.getStartDate())) {
            currentDate = weeklySchedule.getStartDate();
        }

        LocalDate endDate = weeklySchedule.getEndDate();

        List<ReminderOccurrence> occurrences = new ArrayList<>();

        while (!currentDate.isAfter(
                endDate != null ? endDate : until.atZone(zoneId).toLocalDate()
        )) {

            int dayOfWeek = currentDate.getDayOfWeek().getValue();

            if (weeklySchedule.getWeekdays().contains(dayOfWeek)) {

                Instant scheduledAt = currentDate
                        .atTime(weeklySchedule.getReminderTime())
                        .atZone(zoneId)
                        .toInstant();

                if (scheduledAt.isAfter(now) && !scheduledAt.isAfter(until)) {

                    occurrences.add(
                            ReminderOccurrence.builder()
                                    .reminder(reminder)
                                    .scheduledAt(scheduledAt)
                                    .status(OccurrenceStatus.PENDING)
                                    .build()
                    );
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        log.info(
                "Generated {} weekly occurrences for reminder {}",
                occurrences.size(),
                reminder.getId()
        );

        return occurrences;
    }


    @Override
    public Schedule findByReminderId(Reminder reminder) {
        return weeklyScheduleRepository
                .findByReminderId(reminder.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Weekly schedule not found for reminder: "
                                + reminder.getId()
                ));
    }
}