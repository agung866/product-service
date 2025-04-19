package com.example.transaction_service.events;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record SuccessPaymentEvent(  String trxId,
        Long productId, int qty){
}
