package com.ermapsh.notifier.gateway.controller;


import com.ermapsh.notifier.gateway.dto.request.PaymentInitRequest;
import com.ermapsh.notifier.gateway.dto.response.PaymentResponse;
import com.ermapsh.notifier.gateway.service.PaymentService;
import com.ermapsh.notifier.common.dto.res.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Value("${payment.gateway.merchantId}")
    private UUID merchantId; // this is merchantId that allow by bank or any other service provider

    @PostMapping("")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiate(@Valid @RequestBody PaymentInitRequest request) {
        return ApiResponse.created(
                "Payment initiated",
                paymentService.initiate(merchantId, request)
        );
    }
}
