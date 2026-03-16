package com.example.userSpring.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {

    @Email(message = "{userEmail.Validation}")
    private String userEmail;

    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9 ]+$", message = "{userPlace.Validation}")
    private String userPlace;

    @Min(value = 1, message = "{orderId.min}")
    @Max(value = 50, message = "{orderId.max}")
    private Integer orderId;

    private String password;
}
