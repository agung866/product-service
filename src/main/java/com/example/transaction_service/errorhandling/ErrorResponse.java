package com.example.transaction_service.errorhandling;



import java.time.LocalDateTime;


public record ErrorResponse(int status, String message, LocalDateTime timestamp) {

}

