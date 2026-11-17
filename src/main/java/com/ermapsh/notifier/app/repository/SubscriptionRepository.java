package com.ermapsh.notifier.app.repository;

import com.ermapsh.notifier.app.entity.Subscription;
import com.ermapsh.notifier.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByUserId(UUID userId);

    Optional<Subscription> findByUser(User user);
}