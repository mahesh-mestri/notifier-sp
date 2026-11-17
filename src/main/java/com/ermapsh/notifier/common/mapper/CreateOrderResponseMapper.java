package com.ermapsh.notifier.common.mapper;

import com.ermapsh.notifier.gateway.dto.response.CreateOrderResponse;
import com.ermapsh.notifier.gateway.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CreateOrderResponseMapper {

    CreateOrderResponse toResponse(Order order);
}
