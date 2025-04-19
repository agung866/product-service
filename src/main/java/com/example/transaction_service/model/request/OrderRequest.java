package com.example.transaction_service.model.request;



import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.LocalDate;
import java.util.List;

@RecordBuilder
public record OrderRequest(
        String email,
        String paymentMethod,
        List<OrderItem> orderItemList

) {
    public record OrderItem( Long productId,
                                 Integer quantity) {

    }
}
