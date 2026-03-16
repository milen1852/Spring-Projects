package com.example.exceptions;

import com.example.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBook(BookNotFoundException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodException(MethodArgumentNotValidException e){

        Map<String, List<String>> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors
                        .computeIfAbsent(error.getField(), k -> new ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)        //403
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Access denied"));
    }

    @ExceptionHandler(AuthenticationException.class)       //401
    public ResponseEntity<Map<String, String>> handleAuth(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Authentication failed"));
    }

//    @ExceptionHandler(ResourceAccessException.class)       //504
//    public ResponseEntity<Map<String, String>> handleTimeout(ResourceAccessException ex){
//
//        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
//                .body(Map.of("error", ex.getMessage()));
//    }

    //For Microservices

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handle4xx(HttpClientErrorException ex){

        return ResponseEntity.status(ex.getStatusCode()).
                body(Map.of("error", ex.getResponseBodyAsString()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleRestClient(RestClientException e){

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(
                        "message", "Book Service not available currently",
                        "error", "BOOK SERVICE NOT AVAILABLE"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message("Exception Occurred : " + e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }
}
