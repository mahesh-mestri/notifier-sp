package com.ermapsh.notifier.app.operations;

import com.ermapsh.notifier.app.repository.ReminderRepository;
import com.ermapsh.notifier.app.scheduler.ScheduleAdapterRouter;
import com.ermapsh.notifier.app.service.ReminderOccurrenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OccurrenceScheduler {
    private final ReminderRepository reminderRepository;

    private final ReminderOccurrenceService  reminderOccurrenceService;
    private final ScheduleAdapterRouter scheduleAdapterRouter;

    //    @Scheduled(fixedDelay = 300000 )
    @Scheduled(fixedDelay = 3000)
    public void generateUpcomingOccurrences() {
        reminderOccurrenceService.generateUpcomingOccurrences();
    }
}
