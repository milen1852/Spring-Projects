package com.example.controller;

import com.example.dto.request.SubscriptionRequestDTO;
import com.example.dto.response.SubscriptionResponseDTO;
import com.example.entity.PlanType;
import com.example.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/plan")
    public ResponseEntity<SubscriptionResponseDTO> addPlan(@Valid @RequestBody SubscriptionRequestDTO requestDTO){

        SubscriptionResponseDTO responseDTO = subscriptionService.addPlan(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/plan/{planType}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getPlanType(@PathVariable PlanType planType){

        List<SubscriptionResponseDTO> responseDTOs = subscriptionService.getPlanType(planType);

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/plan/id/{planId}")
    public ResponseEntity<SubscriptionResponseDTO> getPlanId(@PathVariable Integer planId){

        SubscriptionResponseDTO responseDTOs = subscriptionService.getPlanId(planId);

        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/plan/{planId}")
    public ResponseEntity<SubscriptionResponseDTO> updatePlan(@PathVariable Integer planId,
                                                              @Valid @RequestBody SubscriptionRequestDTO requestDTO){

        SubscriptionResponseDTO responseDTO = subscriptionService.updatePlan(planId, requestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
    }
}
