package com.ermapsh.notifier.app.outbox;


import com.ermapsh.notifier.app.entity.OutboxEvent;
import com.ermapsh.notifier.app.enums.OutboxStatus;
import com.ermapsh.notifier.app.operations.ReminderOccurrenceToOutboxEventProcessor;
import com.ermapsh.notifier.app.repository.OutboxEventRepository;
import com.ermapsh.notifier.app.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxResultHandler resultHandler;
    private final ReminderOccurrenceToOutboxEventProcessor outboxEventProcessor;
    private final UserContext userContext;

    @Scheduled(fixedDelay = 1000)
    public void pollFromReminderOccurrence(){
//        here we need occurrence and put into to outbox
        outboxEventProcessor.fetchBeforeMinuteReminderOccurrence();
    }

    @Scheduled(fixedDelay = 1000)
    public void poll() {

        List<OutboxEvent> pendingEvents = outboxEventRepository.
                findByOutboxStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);
        log.info("-----gotten pendingEvents outbox event: {}-----", pendingEvents.stream().count());
        for (OutboxEvent event : pendingEvents) {
            try {
                String key = userContext.getUserId().toString();

                Map<String, Object> envelop = Map.of(
                        "eventType", event.getEventType(),
                        "aggregateType", event.getAggregateType(),
                        "aggregateId", event.getAggregateId().toString(),
                        "data", event.getPayload()
                );

//                need to push notification


                resultHandler.handleEventPublished(event);
            } catch (Exception e) {
                log.error("Outbox event failed, eventId: {}, attempts: {}", event.getId(), event.getAttempts());
                resultHandler.handleEventFailed(event, e.getMessage());
            }
        }

    }

    private String extractMerchantId(Map<String, Object> payload) {
        Object val = payload.get("merchantId");
        return val != null ? val.toString() : "unknown";
    }
}
