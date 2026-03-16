package com.example.exception;

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

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EmployeeException.class,
            EmployeeNotFoundException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex,
//                                                                      HttpServletRequest request){
//        String message = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + " : " + error.getDefaultMessage())
//                .collect(Collectors.joining(", "));
//
//        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
//                .timestamp(LocalDateTime.now())
//                .message(message)
//                .path(request.getRequestURI())
//                .build();
//
//        return new ResponseEntity<>(errorResponseDTO.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors
                        .computeIfAbsent(error.getField(), k -> new ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalException(IllegalArgumentException e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message("Error Occurred : " + e.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message("Unexpected Error Occurred : " + e.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
