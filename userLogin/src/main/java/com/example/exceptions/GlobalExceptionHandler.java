package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,  String>> handleRuntime(UserNotFoundException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getLocalizedMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String,  String>> handleRuntime(UserException e){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<Map<String, String>> handleKeycloakUser(KeycloakException e){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String,  String>> handleRuntime(TokenException e){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidation(MethodArgumentNotValidException ex){

        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
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

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleClientError(HttpClientErrorException e){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleRestClient(RestClientException e){

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(
                        "message", "Keycloak Service not available currently",
                        "error", "Keycloak Service is Down"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e){

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                Map.of("error", e.getMessage())
        );
    }
}
