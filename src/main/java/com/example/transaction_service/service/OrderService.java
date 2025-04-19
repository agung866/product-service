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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class OrderService {
    @Autowired
    ProductViewRepository productViewRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public OrderResponse execute(OrderRequest request) {
        AtomicReference<String> trxId = new AtomicReference<>("");
        var statusPayment = "UNPAID";
        var total=request.orderItemList().stream()
                .map(orderItem -> {
                    var product = productViewRepository.findByProductId(orderItem.productId()).orElseThrow(() -> new DataNotFoundException("Data Product Tidak ada"));
                    if (product.getStock() < orderItem.quantity()) {
                        throw new BadRequestException("Stock yang yang tersedia kurang dari yang anda minta");
                    }
                trxId.set("TRX" + UUID.randomUUID());
                    var totalPrice = new BigDecimal(orderItem.quantity()).multiply(product.getPrice());

                    Transaction transaction = Transaction.builder()
                            .email(request.email())
                            .transactionId(trxId.get())
                            .paymentMethod(request.paymentMethod())
                            .status(statusPayment)
                            .totalPrice(totalPrice)
                            .productId(product.getProductId())
                            .quantity(orderItem.quantity())
                            .build();
                    transactionRepository.save(transaction);

                    return totalPrice;
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
        return OrderResponseBuilder.builder()
                .TransactionId(trxId.get())
                .email(request.email())
                .statusPayment(statusPayment)
                .paymentMethod(request.paymentMethod())
                .totalPrice(total)
                .build();
    }
}