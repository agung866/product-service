package com.example.demo.service;

import com.example.demo.errorhandling.DataNotFoundException;
import com.example.demo.model.projection.TransactionResultProjection;
import com.example.demo.model.response.GetAllTransactionResponse;
import com.example.demo.model.response.GetAllTransactionResponseBuilder;
import com.example.demo.model.response.GetAllTransactionResponseItemBuilder;
import com.example.demo.repository.ProductViewRepository;
import com.example.demo.repository.TransactionItemRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAllTransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductViewRepository productViewRepository;
    private final TransactionItemRepository transactionItemRepository;

    @Cacheable(value = "transactions", key = "#startDate + '-' + #endDate + '-' + #status")
    public List<GetAllTransactionResponse> execute(LocalDate startDate, LocalDate endDate, String status) {
        var trx = transactionRepository.findByTransactionDate(startDate, endDate, status);
        if (trx.isEmpty()) {
            throw new DataNotFoundException("Data Tidak Ditemukan");
        }
        Map<String, List<TransactionResultProjection>> groupingByTransactionId =
                trx.stream().collect(Collectors.groupingBy(TransactionResultProjection::getTransactionId));

        return groupingByTransactionId.entrySet().stream().map(entry->{
            var trxId =entry.getKey();
            List<TransactionResultProjection> transactionItems = entry.getValue();
            TransactionResultProjection first = transactionItems.get(0);

            List<GetAllTransactionResponse.Item> items=transactionItems.stream()
                    .map(i-> GetAllTransactionResponseItemBuilder.builder()
                            .productId(i.getProductId())
                            .productName(i.getProductName())
                            .price(i.getPrice())
                            .qty(i.getQuantity())
                            .status(i.getStatus())
                            .build())
                    .toList();
            return GetAllTransactionResponseBuilder.builder()
                    .transactionId(trxId)
                    .email(first.getEmail())
                    .totalPrice(first.getTotalPrice())
                    .listItem(items)
                    .build();
        }).toList();
    }

}
