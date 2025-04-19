package com.example.transaction_service.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record OrderResponse(
        Long orderId,
        String email,
        Long productId,
        Integer quantity,
        BigDecimal totalPrice,
        String paymentMethod,
        String statusPayment
) {}