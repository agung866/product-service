package com.example.demo.service;

import com.example.demo.errorhandling.BadRequestException;
import com.example.demo.errorhandling.DataNotFoundException;
import com.example.demo.repository.TransactionItemRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.example.common.events.SuccessPaymentEvent;
import org.example.common.events.SuccessPaymentEventBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final KafkaTemplate<String, SuccessPaymentEvent> kafkaTemplate;
    @Value("${spring.kafka.topic.payment-success}")
    private String paymentTopic;

    public void execute(String transactionId) {

        transactionRepository.findByTransactionId(transactionId)
                .map(transaction -> {
                    if (transaction.getStatus().equalsIgnoreCase("PAID")) {
                        throw new BadRequestException("Transaction sudah di bayar");
                    }
                    transaction.setStatus("PAID");
                    transactionRepository.save(transaction);

                    List<SuccessPaymentEvent.ItemDetail> items = transactionItemRepository.findByTransactionId(transactionId)
                            .stream()
                            .map(trxItem ->
                                    new SuccessPaymentEvent.ItemDetail(trxItem.getProductId(), trxItem.getQuantity(), trxItem.getPrice(), trxItem.getTotalPrice())

                            ).toList();
                    kafkaTemplate.send(paymentTopic,
                            transactionId,
                            SuccessPaymentEventBuilder.builder()
                                    .email(transaction.getEmail())
                                    .paymentMethod(transaction.getPaymentMethod())
                                    .transactionId(transactionId)
                                    .items(items)
                                    .build());
                    return transaction;
                }).orElseThrow(() -> new DataNotFoundException("Transaksi tidak di temukan atau sudah di bayar"));


    }
}
