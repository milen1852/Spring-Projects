package com.example.service;

import com.example.dto.request.SubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDTO;
import com.example.entity.PlanType;
import com.example.entity.SubscriptionPlan;
import com.example.exception.PlanTypeException;
import com.example.exception.PriceFoundException;
import com.example.mapper.PlanMapper;
import com.example.repository.SubscriptionPlanRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionPlanRepository planRepository;

    private final PlanMapper planMapper;

    @Transactional
    public SubscriptionResponseDTO addPlan(@Valid SubscriptionRequestDTO requestDTO) {

        if (planRepository.existsByPrice(requestDTO.getPrice()))
            throw new PriceFoundException("Plan with same price already exists");

        if(requestDTO.getPlanType().equals(PlanType.MONTHLY))
            requestDTO.setDurationDays(30);
        else
            requestDTO.setDurationDays(365);

        SubscriptionPlan plan = planMapper.convertPlanToEntity(requestDTO);

        SubscriptionPlan savedPlan = planRepository.save(plan);

        return planMapper.convertPlanToResponse(savedPlan);
    }

    public List<SubscriptionResponseDTO> getPlanType(PlanType planType) {

        List<SubscriptionPlan> plans = planRepository.findAllByPlanType(planType);

        return plans.stream()
                .map(planMapper::convertPlanToResponse).toList();
    }

    public SubscriptionResponseDTO getPlanId(Integer planId){

        SubscriptionPlan plan = planRepository.findById(planId).orElseThrow(() ->
                new PlanTypeException("Plan ID does not exists"));

        return planMapper.convertPlanToResponse(plan);
    }

    @Transactional
    public SubscriptionResponseDTO updatePlan(Integer planId, SubscriptionRequestDTO requestDTO) {

        SubscriptionPlan plan = planRepository.findById(planId).orElseThrow(() ->
                new PlanTypeException("Plan ID does not exist to update"));

        plan.setPlanType(requestDTO.getPlanType());
        plan.setPrice(requestDTO.getPrice());

        SubscriptionPlan savedPlan = planRepository.save(plan);

        return planMapper.convertPlanToResponse(savedPlan);
    }
}
