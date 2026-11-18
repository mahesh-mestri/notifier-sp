package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.OnceScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.OnceSchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.repository.OnceScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OnceScheduleAdapter implements ScheduleAdapter {

    private final OnceScheduleRepository onceScheduleRepository;

    @Override
    public Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule
    ) {
        if (!(schedule instanceof OnceScheduleRequest(
                java.time.LocalDate reminderDate, java.time.LocalTime reminderTime, String timeZone
        ))) {
            throw new IllegalArgumentException(
                    "Invalid schedule request for ONCE reminder"
            );
        }

        OnceSchedule onceSchedule = OnceSchedule.builder()
                .reminder(reminder)
                .reminderDate(reminderDate)
                .reminderTime(reminderTime)
                .timeZone(timeZone)
                .build();

        return onceScheduleRepository.save(onceSchedule);
    }
}