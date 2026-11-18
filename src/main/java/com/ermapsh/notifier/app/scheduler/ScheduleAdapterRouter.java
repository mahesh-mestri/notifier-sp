
package com.ermapsh.notifier.app.scheduler;

import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.dto.res.ReminderResponse;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.Schedule;
import com.ermapsh.notifier.app.enums.ReminderScheduleType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    public Schedule create(Reminder reminder, ReminderScheduleRequest schedule) {
        ReminderScheduleType type = reminder.getScheduleType();

        ScheduleAdapter adapter = scheduleAdapters.get(type);

        if (adapter == null) {
            throw new IllegalArgumentException(
                    "No schedule adapter registered for: " + type
            );
        }

        return adapter.create(reminder, schedule);
    }
}
