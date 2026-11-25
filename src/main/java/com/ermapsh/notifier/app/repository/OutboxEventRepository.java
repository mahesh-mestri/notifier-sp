package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.OutboxEvent;
import com.ermapsh.notifier.app.enums.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByOutboxStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);
}