package com.example.mapper;

import com.example.dto.request.SubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDTO;
import com.example.entity.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    public SubscriptionPlan convertPlanToEntity(SubscriptionRequestDTO requestDTO){

        return SubscriptionPlan.builder()
                .planType(requestDTO.getPlanType())
                .price(requestDTO.getPrice())
                .durationDays(requestDTO.getDurationDays())
                .build();
    }

    public SubscriptionResponseDTO convertPlanToResponse(SubscriptionPlan plan){

        return SubscriptionResponseDTO.builder()
                .planId(plan.getPlanId())
                .planType(plan.getPlanType())
                .price(plan.getPrice())
                .durationDays(plan.getDurationDays())
                .build();
    }
}
