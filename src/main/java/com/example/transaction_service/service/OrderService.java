package com.example.transaction_service.service;

import com.example.transaction_service.errorhandling.BadRequestException;
import com.example.transaction_service.errorhandling.DataNotFoundException;
import com.example.transaction_service.model.entity.Transaction;
import com.example.transaction_service.model.request.OrderRequest;
import com.example.transaction_service.model.response.OrderResponse;
import com.example.transaction_service.model.response.OrderResponseBuilder;
import com.example.transaction_service.repository.ProductViewRepository;
import com.example.transaction_service.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;


@Service
public class OrderService {
    @Autowired
    ProductViewRepository productViewRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public OrderResponse execute(OrderRequest request) {
        return productViewRepository.findByProductId(request.productId()).map(product1 -> {
            if (product1.getStock() < request.quantity()) {
                throw new BadRequestException("Stock yang yang tersedia kurang dari yang anda minta");
            }
            var totalPrice = new BigDecimal(request.quantity()).multiply(product1.getPrice());
            Transaction transaction = Transaction.builder()
                    .email(request.email())
                    .quantity(request.quantity())
                    .productId(request.productId())
                    .paymentMethod(request.paymentMethod())
                    .status("UNPAID")
                    .totalPrice(totalPrice)
                    .build();
            transactionRepository.save(transaction);
            return OrderResponseBuilder.builder()
                    .email(request.email())
                    .quantity(request.quantity())
                    .totalPrice(totalPrice)
                    .productId(request.productId())
                    .paymentMethod("Transfer")
                    .statusPayment("UNPAID")
                    .build();
        }).orElseThrow(() -> new DataNotFoundException("Product yang anda cari tidak ada"));
    }
}
