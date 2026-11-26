package com.ermapsh.notifier.app.outbox;

import com.ermapsh.notifier.app.entity.OutboxEvent;
import com.ermapsh.notifier.app.enums.OutboxStatus;
import com.ermapsh.notifier.app.operations.ReminderOccurrenceToOutboxEventProcessor;
import com.ermapsh.notifier.app.repository.OutboxEventRepository;
import com.ermapsh.notifier.app.service.FirebaseNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxResultHandler resultHandler;
    private final ReminderOccurrenceToOutboxEventProcessor outboxEventProcessor;
    private final FirebaseNotificationService firebaseNotificationService;

    @Scheduled(fixedDelay = 60000)
    public void pollFromReminderOccurrence() {
        outboxEventProcessor.fetchBeforeMinuteReminderOccurrence();
    }

    @Scheduled(fixedDelay = 60000)
    public void poll() {
        List<OutboxEvent> pendingEvents = outboxEventRepository
                .findByOutboxStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        log.info("Found {} pending outbox events", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                Map<String, Object> notificationPayload = extractNotificationPayload(event);

                firebaseNotificationService.sendNotification(notificationPayload);

                resultHandler.handleEventPublished(event);
            } catch (Exception e) {
                log.error(
                        "Outbox event failed, eventId: {}, attempts: {}",
                        event.getId(),
                        event.getAttempts(),
                        e
                );
                resultHandler.handleEventFailed(event, e.getMessage());
            }
        }
    }

    private Map<String, Object> extractNotificationPayload(OutboxEvent event) {
        Object rawPayload = event.getPayload();
        if (!(rawPayload instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException(
                    "Unsupported outbox payload type for event " + event.getId()
            );
        }

        Map<String, Object> payload = new HashMap<>();
        rawMap.forEach((k, v) -> payload.put(String.valueOf(k), v));
        return payload;
    }
}