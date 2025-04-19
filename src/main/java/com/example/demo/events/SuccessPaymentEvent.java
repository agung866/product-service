package com.example.demo.events;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record SuccessPaymentEvent(  String trxId,
        Long productId, int qty){
}
