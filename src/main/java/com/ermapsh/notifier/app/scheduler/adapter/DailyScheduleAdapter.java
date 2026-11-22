package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.DailyScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.DailySchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.repository.DailyScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

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
    public List<ReminderOccurrence> generateOccurrences(Reminder reminder, Schedule schedule, Instant from, Instant until) {
        return List.of();
    }
}