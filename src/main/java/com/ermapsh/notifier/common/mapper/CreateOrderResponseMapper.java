package com.ermapsh.notifier.common.mapper;

import com.ermapsh.notifier.gateway.dto.response.CreateOrderResponse;
import com.ermapsh.notifier.gateway.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateOrderResponseMapper {
    CreateOrderResponse toResponse(Order order);
}
