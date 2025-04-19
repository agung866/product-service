package com.example.demo.service;

import com.example.demo.errorhandling.BadRequestException;
import com.example.demo.errorhandling.DataNotFoundException;
import com.example.demo.model.entity.Transaction;
import com.example.demo.model.entity.TransactionItem;
import com.example.demo.model.request.OrderRequest;
import com.example.demo.model.response.OrderResponse;
import com.example.demo.model.response.OrderResponseBuilder;
import com.example.demo.repository.ProductViewRepository;
import com.example.demo.repository.TransactionItemRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductViewRepository productViewRepository;

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;

    public OrderResponse execute(OrderRequest request) {
        var trxId = "TRX-" + UUID.randomUUID();
        var statusPayment = "UNPAID";
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
        List<TransactionItem> transactionItems = request.orderItemList().stream()
                .map(orderItem -> {
                    var product = productViewRepository.findByProductId(orderItem.productId()).orElseThrow(() -> new DataNotFoundException("Data Product Tidak ada"));
                    if (product.getStock() < orderItem.quantity()) {
                        throw new BadRequestException("Stock yang yang tersedia kurang dari yang anda minta");
                    }
                    BigDecimal totalPricePerProduct = BigDecimal.valueOf(orderItem.quantity()).multiply(product.getPrice());
                    totalPrice.set(totalPrice.get().add(totalPricePerProduct));
                    return TransactionItem.builder()
                            .productId(orderItem.productId())
                            .quantity(orderItem.quantity())
                            .price(product.getPrice())
                            .transactionId(trxId)
                            .totalPrice(totalPricePerProduct)
                            .build();
                }).toList();
        Transaction transaction = Transaction.builder()
                .email(request.email())
                .transactionId(trxId)
                .paymentMethod(request.paymentMethod())
                .status(statusPayment)
                .totalPrice(totalPrice.get())
                .build();
        transactionRepository.save(transaction);
        transactionItemRepository.saveAll(transactionItems);
        return OrderResponseBuilder.builder()
                .TransactionId(trxId)
                .email(request.email())
                .statusPayment(statusPayment)
                .paymentMethod(request.paymentMethod())
                .totalPrice(totalPrice.get())
                .build();
    }
}