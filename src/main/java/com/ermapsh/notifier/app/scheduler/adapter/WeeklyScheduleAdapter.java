package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.dto.req.WeeklyScheduleRequest;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.entity.WeeklySchedule;
import com.ermapsh.notifier.app.repository.WeeklyScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

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
    public List<ReminderOccurrence> generateOccurrences(Reminder reminder, Schedule schedule, Instant from, Instant until) {
        return List.of();
    }
}