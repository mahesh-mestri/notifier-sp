package com.ermapsh.notifier.common.mapper;

import com.ermapsh.notifier.gateway.dto.response.PaymentResponse;
import com.ermapsh.notifier.gateway.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    @Mapping(target = "orderId", source = "order.id")
    List<PaymentResponse>  toResponseList(List<Payment> paymentsList);

}
