package com.ermapsh.notifier.gateway.dto.response;

import com.ermapsh.notifier.common.entity.Money;
import com.ermapsh.notifier.gateway.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse{
    UUID id;
    String receipt;
    Money amount;
    OrderStatus status;
    Integer attempts;
    Map<String, Object> notes;
    LocalDateTime expiresAt;
    LocalDateTime createdAt;
}