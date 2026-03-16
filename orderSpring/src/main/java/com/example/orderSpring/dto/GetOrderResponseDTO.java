package com.example.orderSpring.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderResponseDTO {

    private Integer orderId;

    private Integer productId;

    private Integer warehouseId;

    private Integer orderQuantity;
}
