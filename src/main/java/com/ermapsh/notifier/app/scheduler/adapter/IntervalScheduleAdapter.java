package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.dto.req.IntervalScheduleRequest;
import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.IntervalSchedule;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.repository.IntervalScheduleRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class IntervalScheduleAdapter implements ScheduleAdapter {

    private final IntervalScheduleRepository intervalScheduleRepository;

    @Override
    public Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule
    ) {
        if (!(schedule instanceof IntervalScheduleRequest request)) {
            throw new IllegalArgumentException(
                    "Invalid schedule request for INTERVAL reminder"
            );
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
    public List<ReminderOccurrence> generateOccurrences(Reminder reminder, Schedule schedule, Instant from, Instant until) {
        return List.of();
    }
}