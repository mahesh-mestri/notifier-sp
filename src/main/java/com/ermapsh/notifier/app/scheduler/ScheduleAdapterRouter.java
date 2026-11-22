
package com.ermapsh.notifier.app.scheduler;

import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.enums.ReminderScheduleType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleAdapterRouter {

    private final Map<ReminderScheduleType, ScheduleAdapter> scheduleAdapters;

    public Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule) {

        ReminderScheduleType type = reminder.getScheduleType();

        ScheduleAdapter adapter = getAdapter(type);

        return adapter.create(reminder, schedule);
    }

    public List<ReminderOccurrence> generateOccurrences(
            Reminder reminder,
            Schedule schedule,
            Instant from,
            Instant until) {

        ReminderScheduleType type = reminder.getScheduleType();

        ScheduleAdapter adapter = getAdapter(type);

        return adapter.generateOccurrences(
                reminder,
                schedule,
                from,
                until
        );
    }

    private ScheduleAdapter getAdapter(ReminderScheduleType type) {

        ScheduleAdapter adapter = scheduleAdapters.get(type);

        if (adapter == null) {
            throw new IllegalArgumentException(
                    "No schedule adapter registered for: " + type
            );
        }

        return adapter;
    }
}
