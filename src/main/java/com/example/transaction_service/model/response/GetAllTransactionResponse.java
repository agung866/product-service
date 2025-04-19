package com.example.transaction_service.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record GetAllTransactionResponse (
        Long transactionId,
        Long productId,
        String productName,
        String email,
        int stock,
        String Status,
        BigDecimal price,
        String imageUrl
){
}
