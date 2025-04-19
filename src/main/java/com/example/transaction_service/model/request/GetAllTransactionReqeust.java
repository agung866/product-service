package com.example.transaction_service.model.request;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.LocalDate;

@RecordBuilder
public record GetAllTransactionReqeust (
        LocalDate startDate,
        LocalDate endDate
){
}
