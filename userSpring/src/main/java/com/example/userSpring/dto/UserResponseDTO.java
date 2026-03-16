package com.example.userSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Integer userId;

    private String userEmail;

    private String userName;

    private String userPlace;

    private Integer orderId;

    private String password;
}
