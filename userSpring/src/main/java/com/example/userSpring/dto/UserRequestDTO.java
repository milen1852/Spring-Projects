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
public class UserRequestDTO {

    @NotNull(message = "{userId.Notnull}")
    @Min(value = 1, message = "{userId.min}")
    @Max(value = 50, message = "{userId.max}")
    private Integer userId;

    @NotBlank(message = "{userName.Notnull}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{userName.Validation}")
    private String userName;

    @NotBlank(message = "{userEmail.Notnull}")
    @Email(message = "{userEmail.Validation}")
    private String userEmail;

    @NotBlank(message = "{userPlace.Notnull}")
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9 ]+$", message = "{userPlace.Validation}")
    private String userPlace;

    @NotNull(message = "{orderId.Notnull}")
    @Min(value = 1, message = "{orderId.min}")
    @Max(value = 50, message = "{orderId.max}")
    private Integer orderId;

    private String password;
}
