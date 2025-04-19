package com.example.demo.errorhandling;



import java.time.LocalDateTime;


public record ErrorResponse(int status, String message, LocalDateTime timestamp) {

}

