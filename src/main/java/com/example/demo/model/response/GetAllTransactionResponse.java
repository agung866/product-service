package com.example.demo.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record GetAllTransactionResponse (
        String transactionId,
        Long productId,
        String productName,
        String email,
        int stock,
        String Status,
        BigDecimal price,
        String imageUrl
){
}
