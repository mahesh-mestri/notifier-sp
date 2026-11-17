package com.ermapsh.notifier.gateway.service.impl;

import com.ermapsh.notifier.app.entity.User;
import com.ermapsh.notifier.app.exception.DuplicateResourceException;
import com.ermapsh.notifier.app.exception.ResourceNotFoundException;
import com.ermapsh.notifier.app.security.UserContext;
import com.ermapsh.notifier.app.repository.UserRepository;
import com.ermapsh.notifier.common.mapper.CreateOrderResponseMapper;
import com.ermapsh.notifier.gateway.dto.request.CreateOrderRequest;
import com.ermapsh.notifier.gateway.dto.response.CreateOrderResponse;
import com.ermapsh.notifier.gateway.entity.Order;
import com.ermapsh.notifier.gateway.repository.OrderRepository;
import com.ermapsh.notifier.gateway.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${payment.order.default_order_expiry_minutes}")
    private int defaultOrderExpiryMinutes;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CreateOrderResponseMapper createOrderResponseMapper;
    private final UserContext userContext;

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        UUID userId = userContext.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + userId
                        )
                );

        if (request.receipt() != null &&
                orderRepository.existsByUserIdAndReceipt(
                        userId,
                        request.receipt()
                )) {

            throw new DuplicateResourceException(
                    "Order with receipt already exists: "
                            + request.receipt()
            );
        }

        Order newOrder = Order.builder()
                .user(user)
                .amount(request.amount())
                .subscriptionPlan(request.subscriptionPlan())
                .receipt(request.receipt())
                .notes(request.notes())
                .expiresAt(
                        request.expiresAt() != null
                                ? request.expiresAt()
                                : LocalDateTime.now()
                                .plusMinutes(defaultOrderExpiryMinutes)
                )
                .build();

        Order savedOrder = orderRepository.save(newOrder);

        return createOrderResponseMapper.toResponse(savedOrder);
    }
}