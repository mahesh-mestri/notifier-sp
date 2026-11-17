package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.dto.req.SubscribeRequest;
import com.ermapsh.notifier.app.dto.res.SubscriptionResponse;
import com.ermapsh.notifier.app.entity.Subscription;
import com.ermapsh.notifier.app.entity.User;
import com.ermapsh.notifier.app.enums.SubscriptionPlan;
import com.ermapsh.notifier.app.enums.SubscriptionStatus;
import com.ermapsh.notifier.app.exception.ResourceNotFoundException;
import com.ermapsh.notifier.app.repository.SubscriptionRepository;
import com.ermapsh.notifier.app.repository.UserRepository;
import com.ermapsh.notifier.app.security.UserContext;
import com.ermapsh.notifier.app.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final UserContext userContext;

    @Override
    @Transactional
    public SubscriptionResponse subscribe(
            UUID userId,
            SubscribeRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userId
                        )
                );

        if (request.plan() == SubscriptionPlan.FREE) {
            throw new IllegalArgumentException(
                    "Cannot subscribe to FREE plan"
            );
        }

        Subscription subscription =
                subscriptionRepository.findByUserId(userId)
                        .orElseGet(() ->
                                Subscription.builder()
                                        .user(user)
                                        .build()
                        );

        Instant now = Instant.now();

        subscription.setPlan(request.plan());
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartedAt(now);
        subscription.setExpiresAt(
                now.plus(30, ChronoUnit.DAYS)
        );
        subscription.setAutoRenewing(false);
        subscription.setCancelledAt(null);

        Subscription saved =
                subscriptionRepository.save(subscription);

        return new SubscriptionResponse(
                saved.getId(),
                saved.getPlan(),
                saved.getStatus(),
                saved.getStartedAt(),
                saved.getExpiresAt(),
                saved.isAutoRenewing(),
                true
        );
    }


}