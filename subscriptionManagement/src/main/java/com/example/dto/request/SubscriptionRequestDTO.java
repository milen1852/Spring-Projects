package com.example.dto.request;

import com.example.entity.PlanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDTO {

    private PlanType planType;

    @NotNull(message = "{price.NotNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{price.MinVal}")
    private Double price;

    private Integer durationDays;
}
