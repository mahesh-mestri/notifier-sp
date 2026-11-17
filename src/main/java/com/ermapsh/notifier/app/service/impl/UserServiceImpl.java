package com.ermapsh.notifier.app.service.impl;

import com.ermapsh.notifier.app.dto.res.SubscriptionResponse;
import com.ermapsh.notifier.app.dto.res.UserProfileResponse;
import com.ermapsh.notifier.app.entity.Subscription;
import com.ermapsh.notifier.app.entity.User;
import com.ermapsh.notifier.app.enums.SubscriptionPlan;
import com.ermapsh.notifier.app.enums.SubscriptionStatus;
import com.ermapsh.notifier.app.exception.ResourceNotFoundException;
import com.ermapsh.notifier.app.repository.SubscriptionRepository;
import com.ermapsh.notifier.app.repository.UserRepository;
import com.ermapsh.notifier.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public UserProfileResponse getProfile(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userId
                        )
                );

        Subscription subscription =
                subscriptionRepository.findByUserId(userId).orElse(null);

        if (subscription == null) {
            return new UserProfileResponse(
                    user.getId(),
                    user.getEmail(),
                    new SubscriptionResponse(
                            null,
                            SubscriptionPlan.FREE,
                            SubscriptionStatus.ACTIVE,
                            null,
                            null,
                            false,
                            false
                    )
            );
        }

        boolean isPro =
                subscription.getPlan() != SubscriptionPlan.FREE
                        && subscription.getStatus() == SubscriptionStatus.ACTIVE
                        && subscription.getExpiresAt() != null
                        && subscription.getExpiresAt().isAfter(Instant.now());

        SubscriptionResponse subscriptionResponse =
                new SubscriptionResponse(
                        subscription.getId(),
                        subscription.getPlan(),
                        subscription.getStatus(),
                        subscription.getStartedAt(),
                        subscription.getExpiresAt(),
                        subscription.isAutoRenewing(),
                        isPro
                );

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                subscriptionResponse
        );
    }
}