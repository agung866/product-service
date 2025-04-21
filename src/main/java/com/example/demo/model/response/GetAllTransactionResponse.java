package com.example.demo.model.response;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@RecordBuilder
public record GetAllTransactionResponse (
        String transactionId,
        String email,
        String totalPrice,
        List<Item> listItem
)implements Serializable {
    @RecordBuilder
    public record Item(
            Long productId,
            String productName,
            int qty,
            BigDecimal price,
            String status
    )implements Serializable{

    }
}
