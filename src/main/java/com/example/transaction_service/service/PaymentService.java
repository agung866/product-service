package com.example.transaction_service.service;

import com.example.transaction_service.errorhandling.BadRequestException;
import com.example.transaction_service.errorhandling.DataNotFoundException;
import com.example.transaction_service.events.SuccessPaymentEvent;
import com.example.transaction_service.events.SuccessPaymentEventBuilder;
import com.example.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, SuccessPaymentEvent> kafkaTemplate;
    @Value("${spring.kafka.topic.payment-success}")
    private String paymentTopic;
    public void execute(String transactionId){
        transactionRepository.findByTransactionId(transactionId)
                .map(transaction -> {
                    if(transaction.getStatus().equalsIgnoreCase("PAID")){
                        throw new BadRequestException("Transaction sudah di bayar");
                    }
                  transaction.setStatus("PAID");
                    transactionRepository.save(transaction);
                    kafkaTemplate.send(paymentTopic,
                            String.valueOf(transactionId),
                            SuccessPaymentEventBuilder.builder()
                                    .trxId(transactionId)
                                    .productId(transaction.getProductId())
                                    .qty(transaction.getQuantity())
                                    .build());
                    return transaction;
                }).orElseThrow(()->new DataNotFoundException("Transaksi tidak di temukan atau sudah di bayar"));


    }
}
