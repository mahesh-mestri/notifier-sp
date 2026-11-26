
package com.ermapsh.notifier.app.operations;

import com.ermapsh.notifier.app.entity.ReminderOccurrence;
import com.ermapsh.notifier.app.enums.EventAggregateType;
import com.ermapsh.notifier.app.enums.OccurrenceStatus;
import com.ermapsh.notifier.app.outbox.OutboxEventPublisher;
import com.ermapsh.notifier.app.repository.ReminderOccurrenceRepository;
import com.ermapsh.notifier.app.service.FirebaseNotificationService;
import com.ermapsh.notifier.app.service.ReminderOccurrenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderOccurrenceToOutboxEventProcessor {

    private final ReminderOccurrenceRepository reminderOccurrenceRepository;
    private final OutboxEventPublisher outboxEventPublisher;
    private final ReminderOccurrenceService reminderOccurrenceService;
    private final FirebaseNotificationService firebaseNotificationService;

    @Transactional
    public void occurrencesToOutboxEvent() {

        // 1. Fetch occurrences due within the next minute
        List<ReminderOccurrence> occurrences =
                fetchBeforeMinuteReminderOccurrence();

        log.info("occurrences has count for outbox: {}", occurrences.size());
        if (occurrences.isEmpty()) {
            log.info("No occurrences due within next minute");
            return;
        }

        // 2. Publish each occurrence as an Outbox event
        for (ReminderOccurrence occurrence : occurrences) {

            outboxEventPublisher.publish(
                    EventAggregateType.REMINDER_OCCURRENCE,
                    occurrence.getId(),
                    "FCM_NOTIFICATION",
                    Map.of(
                            "occurrenceId", occurrence.getId().toString(),
                            "scheduledAt", occurrence.getScheduledAt().toString(),
                            "status", occurrence.getStatus().name(),
                            "reminderId", occurrence.getReminder().getId().toString(),
                            "title", occurrence.getReminder().getTitle(),
                            "notificationEnabled", occurrence.getReminder().isNotificationEnabled()
                    )
            );

            // 3. Mark occurrence as queued
            occurrence.setStatus(OccurrenceStatus.QUEUED);
        }

        // 4. Persist occurrence status updates
        reminderOccurrenceRepository.saveAll(occurrences);

        log.info(
                "Created {} outbox events and queued {} occurrences",
                occurrences.size(),
                occurrences.size()
        );
    }

    public List<ReminderOccurrence> fetchBeforeMinuteReminderOccurrence() {
        return reminderOccurrenceService
                .findOccurrencesDueWithinNextMinute();
    }
}
