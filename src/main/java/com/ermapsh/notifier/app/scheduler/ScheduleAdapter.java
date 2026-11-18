package com.ermapsh.notifier.app.scheduler;

import com.ermapsh.notifier.app.entity.Reminder;

import java.time.Instant;
import java.util.List;

public interface ScheduleAdapter {

    List<Instant> generate(Reminder reminder, Instant from, Instant until);

}
