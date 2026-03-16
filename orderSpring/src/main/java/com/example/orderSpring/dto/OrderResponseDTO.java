package com.example.orderSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Integer productId;

    private Integer orderQuantity;

    private String status;

    private String message;

    private Integer remainingQuantity;
}
