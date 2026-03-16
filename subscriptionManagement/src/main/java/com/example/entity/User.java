package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @EmbeddedId
    private UserKey key;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "plan_type")
    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "subscription_status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @Column(name = "subscription_start_date")
    private LocalDate subscriptionStartDate;

    @Column(name = "subscription_end_date")
    private LocalDate subscriptionEndDate;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate(){ createdAt = LocalDateTime.now(); }

    @PreUpdate
    void onUpdate(){ updatedAt = LocalDateTime.now(); }
}
