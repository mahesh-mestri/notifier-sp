package com.ermapsh.notifier.app.config;

import com.ermapsh.notifier.app.enums.ReminderScheduleType;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapter;
import com.ermapsh.notifier.app.scheduler.adapter.DailyScheduleAdapter;
import com.ermapsh.notifier.app.scheduler.adapter.IntervalScheduleAdapter;
import com.ermapsh.notifier.app.scheduler.adapter.MonthlyScheduleAdapter;
import com.ermapsh.notifier.app.scheduler.adapter.OnceScheduleAdapter;
import com.ermapsh.notifier.app.scheduler.adapter.WeeklyScheduleAdapter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ScheduleAdapterConfig {

    private final OnceScheduleAdapter onceScheduleAdapter;
    private final IntervalScheduleAdapter intervalScheduleAdapter;
    private final DailyScheduleAdapter dailyScheduleAdapter;
    private final WeeklyScheduleAdapter weeklyScheduleAdapter;
    private final MonthlyScheduleAdapter monthlyScheduleAdapter;

    @Bean
    public Map<ReminderScheduleType, ScheduleAdapter> scheduleAdapterMap() {

        return Map.of(
                ReminderScheduleType.ONCE, onceScheduleAdapter,
                ReminderScheduleType.INTERVAL, intervalScheduleAdapter,
                ReminderScheduleType.DAILY, dailyScheduleAdapter,
                ReminderScheduleType.WEEKLY, weeklyScheduleAdapter,
                ReminderScheduleType.MONTHLY, monthlyScheduleAdapter
        );
    }
}
