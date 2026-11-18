package com.ermapsh.notifier.app.scheduler.adapter;

import com.ermapsh.notifier.app.entity.Reminder;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class IntervalScheduleAdapter implements ScheduleAdapter {
    @Override
    public List<Instant> generate(Reminder reminder, Instant from, Instant until) {
        return List.of();
    }
}
