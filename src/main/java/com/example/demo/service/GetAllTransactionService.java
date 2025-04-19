package com.example.demo.service;

import com.example.demo.errorhandling.DataNotFoundException;
import com.example.demo.model.response.GetAllTransactionResponse;
import com.example.demo.model.response.GetAllTransactionResponseBuilder;
import com.example.demo.repository.ProductViewRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
//        var productIds = trx.stream()
//                .map(Transaction::getProductId)
//                .distinct().toList();

//        var product = productViewRepository.findAllByProductIdIn(productIds);
//        Map<Long, Product> toMap = product.stream()
//                .collect(Collectors.toMap(Product::getProductId, Function.identity()));
return null;
//        return trx.stream().map(transaction -> {
////            Product products = toMap.get(transaction.getProductId());
//            return GetAllTransactionResponseBuilder.builder()
//                    .transactionId(transaction.getTransactionId())
//                    .productId(products.getProductId())
//                    .productName(products.getProductName())
//                    .price(transaction.getTotalPrice())
//                    .stock(products.getStock())
//                    .email(transaction.getEmail())
//                    .Status(transaction.getStatus())
//                    .build();
//        }).toList();
    }

}
