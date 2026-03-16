package com.example.orderSpring.exceptionHandler;

import com.example.orderSpring.customException.InsufficientStockException;
import com.example.orderSpring.customException.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.orderSpring.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InsufficientStockException.class, OrderNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleStockException(RuntimeException runtimeException,
                                                              HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(runtimeException.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Unexpected error occurred: " + ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
