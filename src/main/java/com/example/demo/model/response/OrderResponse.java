package com.example.demo.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record OrderResponse(
        String TransactionId,
        String email,
        BigDecimal totalPrice,
        String paymentMethod,
        String statusPayment
) {}