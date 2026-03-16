package com.example.mapper;

import com.example.dto.request.UserRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.entity.SubscriptionPlan;
import com.example.entity.User;
import com.example.entity.UserKey;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User convertUserToEntity(UserRequestDTO requestDTO){

        return User.builder()
                .key(UserKey.builder()
                        .userId(requestDTO.getUserId())               //Composite Key
                        .firstName(requestDTO.getFirstName())
                        .email(requestDTO.getEmail())
                        .build())
                .lastName(requestDTO.getLastName())
                .phoneNumber(requestDTO.getPhoneNumber())
                .planType(requestDTO.getPlanType())
                .price(requestDTO.getPrice())
                .subscriptionPlan(SubscriptionPlan.builder()          //Foreign Key
                        .planId(requestDTO.getPlanId())
                        .build())
                .subscriptionStatus(requestDTO.getSubscriptionStatus())
                .subscriptionStartDate(requestDTO.getSubscriptionStartDate())
                .subscriptionEndDate(requestDTO.getSubscriptionEndDate())
                .build();
    }

    public UserResponseDTO convertUserToResponse(User user){

        return UserResponseDTO.builder()
                .userId(user.getKey().getUserId())
                .firstName(user.getKey().getFirstName())
                .email(user.getKey().getEmail())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .planType(user.getPlanType())
                .price(user.getPrice())
                .planId(user.getSubscriptionPlan().getPlanId())
                .subscriptionStatus(user.getSubscriptionStatus())
                .subscriptionStartDate(user.getSubscriptionStartDate())
                .subscriptionEndDate(user.getSubscriptionEndDate())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
