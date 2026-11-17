package com.ermapsh.notifier.gateway.dto.request;


import com.ermapsh.notifier.common.entity.Money;
import com.ermapsh.notifier.gateway.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
