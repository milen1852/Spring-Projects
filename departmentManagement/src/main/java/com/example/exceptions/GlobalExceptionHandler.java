package com.example.exceptions;

import com.example.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DepartmentNameException.class,
            DepartmentNotFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleRunTimeException(RuntimeException e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message("Custom Exception Error : " + e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException e){

        Map<String, List<String>> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors
                        .computeIfAbsent(error.getField(), k -> new ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message("Unexpected Error Occured : " + e.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
