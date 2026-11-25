package com.ermapsh.notifier.app.outbox;

import com.ermapsh.notifier.app.entity.OutboxEvent;
import com.ermapsh.notifier.app.enums.EventAggregateType;
import com.ermapsh.notifier.app.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher {
    private final OutboxEventRepository outboxEventRepository;

    public void publish(EventAggregateType aggregateType, UUID aggregateId, String eventType,
                        Map<String, Object> payload) {
        OutboxEvent outboxEvent = OutboxEvent.builder().
                aggregateId(aggregateId).
                aggregateType(aggregateType).
                eventType(eventType).
                payload(payload).
                build();

        outboxEventRepository.save(outboxEvent);
    }

    public void publishAll(List<OutboxEvent> events) {
        outboxEventRepository.saveAll(events);
    }
}
