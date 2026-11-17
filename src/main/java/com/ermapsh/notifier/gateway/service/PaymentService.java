package com.ermapsh.notifier.gateway.service;

import com.ermapsh.notifier.gateway.dto.request.PaymentInitRequest;
import com.ermapsh.notifier.gateway.dto.response.PaymentResponse;
import java.util.UUID;


public interface PaymentService {
    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);
}
