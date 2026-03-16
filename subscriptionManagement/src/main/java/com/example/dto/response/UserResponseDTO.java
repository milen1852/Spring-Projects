package com.example.dto.response;

import com.example.entity.PlanType;
import com.example.entity.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Integer userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private PlanType planType;

    private Double price;

    private Integer planId;

    private SubscriptionStatus subscriptionStatus;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
