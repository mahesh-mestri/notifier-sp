package com.ermapsh.notifier.app.operations;

import com.ermapsh.notifier.app.repository.ReminderRepository;
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
    private final ReminderOccurrenceToOutboxEventProcessor occurrenceToOutboxEventProcessor;
    //    @Scheduled(fixedDelay = 300000 )
    @Scheduled(fixedDelay = 1000)
    public void generateUpcomingOccurrences() {
        reminderOccurrenceService.generateUpcomingOccurrences();
    }

    @Scheduled(fixedDelay = 1000)
    public void occurrencesToOutboxEvent() {
        occurrenceToOutboxEventProcessor.occurrencesToOutboxEvent();
    }
}
