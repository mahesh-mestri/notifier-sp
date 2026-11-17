package com.ermapsh.notifier.gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ermapsh.notifier.common.entity.Money;
import com.ermapsh.notifier.gateway.enums.PaymentMethod;
import com.ermapsh.notifier.gateway.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        String idempotency,
        String bankReference,
        Money money,
        PaymentMethod paymentMethod,
        Map<String, Object> methodDetails,
        PaymentStatus paymentStatus,
        String errorCode,
        String errorDescription,
        LocalDateTime authorizedAt,
        LocalDateTime capturedAt,
        LocalDateTime failedAt,
        LocalDateTime refundedAt
) {
}