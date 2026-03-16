package com.example.dto.request;

import com.example.entity.PlanType;
import com.example.entity.SubscriptionStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "{userId.NotNull}")
    @Min(value = 1, message = "{userId.MinVal}")
    private Integer userId;

    @NotBlank(message = "{firstName.NotBlank}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{firstName.Pattern}")
    private String firstName;

    @Pattern(regexp = "^$|^[A-Za-z ]+$", message = "{lastName.Pattern}")
    private String lastName;

    @NotBlank(message = "{email.NotBlank}")
    @Email(message = "{email.Pattern}")
    private String email;

    @Pattern(regexp = "^$|^[0-9]{10}$", message = "{phoneNumber.Pattern}")
    private String phoneNumber;

    private PlanType planType;

    @NotNull(message = "{price.NotNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{price.MinVal}")
    private Double price;

    @NotNull(message = "{planId.NotNull}")
    private Integer planId;

    private SubscriptionStatus subscriptionStatus;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;
}
