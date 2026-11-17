package com.ermapsh.notifier.gateway.controller;


import com.ermapsh.notifier.gateway.dto.request.CreateOrderRequest;
import com.ermapsh.notifier.gateway.dto.response.CreateOrderResponse;
import com.ermapsh.notifier.common.dto.res.ApiResponse;
import com.ermapsh.notifier.app.security.UserContext;
import com.ermapsh.notifier.gateway.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserContext userContext;

    @PostMapping("")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return ApiResponse.created("order created successfully", orderService.createOrder(request));
    }
}
