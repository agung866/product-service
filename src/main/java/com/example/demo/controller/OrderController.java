package com.example.demo.controller;

import com.example.demo.model.request.OrderRequest;
import com.example.demo.model.response.GetAllTransactionResponse;
import com.example.demo.model.response.OrderResponse;
import com.example.demo.service.GetAllTransactionService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final GetAllTransactionService getAllTransactionService;


    @PostMapping("/create-order")
    public OrderResponse order(@RequestBody OrderRequest orderRequest) {
        return orderService.execute(orderRequest);
    }

    @PostMapping("/update-payment")
    public void updatePayment(@RequestParam String transactionId) {
        paymentService.execute(transactionId);
    }

    @GetMapping("/getall-payment")
    public List<GetAllTransactionResponse> getAllTrx(@RequestParam LocalDate startDate, LocalDate endDate, String status) {
        return getAllTransactionService.execute(startDate, endDate, status);
    }
}
