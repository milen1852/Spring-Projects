package com.example.dto.response;

import com.example.entity.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {

    private Integer planId;

    private PlanType planType;

    private Double price;

    private Integer durationDays;
}
