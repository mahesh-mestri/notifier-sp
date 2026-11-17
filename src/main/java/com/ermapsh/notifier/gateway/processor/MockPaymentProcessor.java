package com.ermapsh.notifier.gateway.processor;

import com.ermapsh.notifier.gateway.dto.request.PaymentRequest;
import com.ermapsh.notifier.gateway.dto.response.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

// so it will give failed or authorized payment
@Component
public class MockPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentResult authorize(PaymentRequest request) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Payment processing interrupted", e);
        }

        return new PaymentResult.Success("BANK-" + UUID.randomUUID()); // assuming only success here
    }
}