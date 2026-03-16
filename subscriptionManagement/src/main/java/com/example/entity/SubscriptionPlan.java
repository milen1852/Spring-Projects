package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlan{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "plan_type")
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration_days")
    private Integer durationDays;
}
