package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Request DTO - Data Transfer Object for incoming requests
 * 
 * DTOs are used to:
 * 1. Decouple API contract from internal entity structure
 * 2. Control what data clients can send
 * 3. Add validation rules
 * 4. Prevent over-posting (mass assignment vulnerabilities)
 * 
 * This DTO is used for POST /api/users endpoint
 * 
 * @Data - Lombok: Generates getters, setters, toString, equals, hashCode
 * @Builder - Lombok: Enables builder pattern
 * @NoArgsConstructor - Required for JSON deserialization
 * @AllArgsConstructor - Required for builder pattern
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    
    /**
     * User's name
     * @NotBlank - Validates that field is not null and not empty
     * @Size - Validates string length
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    /**
     * User's email
     * @Email - Validates email format
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    /**
     * User's age
     * @NotNull - Field cannot be null
     * @Min/@Max - Validates number range
     */
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;
}
