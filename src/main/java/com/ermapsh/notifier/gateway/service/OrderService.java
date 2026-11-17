package com.ermapsh.notifier.gateway.service;

import com.ermapsh.notifier.gateway.dto.request.CreateOrderRequest;
import com.ermapsh.notifier.gateway.dto.response.CreateOrderResponse;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest receipt);
}
