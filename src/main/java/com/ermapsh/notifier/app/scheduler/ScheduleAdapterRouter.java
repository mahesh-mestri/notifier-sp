
package com.ermapsh.notifier.app.scheduler;

import com.ermapsh.notifier.app.entity.Reminder;
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

    public List<Instant> generate(
            Reminder reminder,
            Instant from,
            Instant until
    ) {

        ReminderScheduleType scheduleType = reminder.getScheduleType();

        log.info(
                "Generating occurrences for reminder: {}, schedule type: {}",
                reminder.getId(),
                scheduleType
        );

        ScheduleAdapter adapter = scheduleAdapters.get(scheduleType);

        if (adapter == null) {
            throw new IllegalArgumentException(
                    "No schedule adapter registered for type: " + scheduleType
            );
        }

        return adapter.generate(reminder, from, until);
    }
}
