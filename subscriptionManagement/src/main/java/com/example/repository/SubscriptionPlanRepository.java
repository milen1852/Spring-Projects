package com.example.repository;

import com.example.entity.PlanType;
import com.example.entity.SubscriptionPlan;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {
    boolean existsByPrice(@NotNull(message = "{price.NotNull}")
                          @Pattern(regexp = "^[1-9]\\d*(\\.\\d{1,2})?$",
                                  message = "{price.Pattern}")
                          Double price);

    List<SubscriptionPlan> findAllByPlanType(PlanType planType);
}
