package com.ermapsh.notifier.app.service;

import com.ermapsh.notifier.app.dto.req.SubscribeRequest;
import com.ermapsh.notifier.app.dto.res.SubscriptionResponse;

import java.util.UUID;

public interface SubscriptionService {
    SubscriptionResponse subscribe(UUID userId, SubscribeRequest request);
}