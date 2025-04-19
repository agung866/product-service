package com.example.transaction_service.events;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record SuccessPaymentEvent(  Long orderId,
        Long productId, int qty){
}
