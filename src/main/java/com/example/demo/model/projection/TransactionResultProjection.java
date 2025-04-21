package com.example.demo.model.projection;

import java.math.BigDecimal;

public interface TransactionResultProjection {
        String getTransactionId();
        String getEmail();
        String getTotalPrice();
        Long getProductId();
        String getProductName();
        int getQuantity();
        BigDecimal getPrice();
        String getStatus();

}
