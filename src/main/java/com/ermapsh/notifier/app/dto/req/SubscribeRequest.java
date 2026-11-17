package com.ermapsh.notifier.app.dto.req;

import com.ermapsh.notifier.app.enums.SubscriptionPlan;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
    @NotNull
    SubscriptionPlan plan
){}
