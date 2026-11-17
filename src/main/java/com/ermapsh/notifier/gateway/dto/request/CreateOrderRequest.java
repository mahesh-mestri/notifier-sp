package com.ermapsh.notifier.gateway.dto.request;

import com.ermapsh.notifier.app.enums.SubscriptionPlan;
import com.ermapsh.notifier.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @NotNull(message = "Subscription plan is required")
        SubscriptionPlan subscriptionPlan,

        @Size(max = 100)
        String receipt,

        Map<String, Object> notes,

        LocalDateTime expiresAt
) {}