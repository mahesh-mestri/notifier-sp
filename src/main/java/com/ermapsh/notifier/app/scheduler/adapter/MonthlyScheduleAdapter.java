package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.MonthlyScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.MonthlySchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.repository.MonthlyScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MonthlyScheduleAdapter implements ScheduleAdapter {

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
    public List<ReminderOccurrence> generateOccurrences(Reminder reminder, Schedule schedule, Instant from, Instant until) {
        return List.of();
    }
}