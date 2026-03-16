package com.example.exception;

import com.example.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${ex.error}")
    private String error;

    @ExceptionHandler({PlanTypeException.class, PriceFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleRunTime(RuntimeException e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(errorResponseDTO);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, String>> handleToken(TokenException e){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                error, e.getMessage()
        ));
    }

    @ExceptionHandler({UserException.class, UserEmailException.class})
    public ResponseEntity<Map<String, String>> handleUser(RuntimeException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(error, e.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleClientError(HttpClientErrorException e){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of(error, "Invalid Credentials")
        );
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleRest(RestClientException e){

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                error, "Keycloak Service is Down"
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodException(MethodArgumentNotValidException e){

        Map<String, List<String>> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(err ->
                errors
                        .computeIfAbsent(err.getField(), k -> new ArrayList<>())
                        .add(err.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
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
