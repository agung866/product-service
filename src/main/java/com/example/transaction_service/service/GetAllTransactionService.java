package com.example.transaction_service.service;

import com.example.transaction_service.errorhandling.DataNotFoundException;
import com.example.transaction_service.model.entity.Product;
import com.example.transaction_service.model.entity.Transaction;
import com.example.transaction_service.model.response.GetAllTransactionResponse;
import com.example.transaction_service.model.response.GetAllTransactionResponseBuilder;
import com.example.transaction_service.repository.ProductViewRepository;
import com.example.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAllTransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductViewRepository productViewRepository;

    @Cacheable(value = "transactions", key = "#startDate.toString() + '-' + #endDate.toString() + '-' + #status")
    public List<GetAllTransactionResponse> execute(LocalDate starDate, LocalDate endDate, String status) {
        var trx = transactionRepository.findByTransactionDate(starDate, endDate, status);
        if (trx.isEmpty()) {
            throw new DataNotFoundException("Data Tidak Ditemukan");
        }
        var productIds = trx.stream()
                .map(Transaction::getProductId)
                .distinct().toList();

        var product = productViewRepository.findAllByProductIdIn(productIds);
        Map<Long, Product> toMap = product.stream()
                .collect(Collectors.toMap(Product::getProductId, Function.identity()));

        return trx.stream().map(transaction -> {
            Product products = toMap.get(transaction.getProductId());
            return GetAllTransactionResponseBuilder.builder()
                    .transactionId(transaction.getTransactionId())
                    .productId(products.getProductId())
                    .productName(products.getProductName())
                    .price(transaction.getTotalPrice())
                    .stock(products.getStock())
                    .email(transaction.getEmail())
                    .Status(transaction.getStatus())
                    .build();
        }).toList();
    }

}
