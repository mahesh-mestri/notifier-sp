package com.ermapsh.notifier.gateway.processor;

import com.ermapsh.notifier.gateway.dto.request.PaymentRequest;
import com.ermapsh.notifier.gateway.dto.response.PaymentResult;

public interface PaymentProcessor {
    PaymentResult authorize(PaymentRequest request);
}