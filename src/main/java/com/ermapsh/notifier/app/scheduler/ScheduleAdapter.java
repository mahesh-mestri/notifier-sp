package com.ermapsh.notifier.app.scheduler;

import com.ermapsh.notifier.app.dto.req.ReminderScheduleRequest;
import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.entity.Schedule;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ScheduleAdapter {

    Schedule create(
            Reminder reminder,
            ReminderScheduleRequest schedule
    );

    List<ReminderOccurrence> arrangeReminderOccurrence(
            Reminder reminder,
            Schedule schedule
    );

    Schedule findByReminderId(Reminder reminder);
}
