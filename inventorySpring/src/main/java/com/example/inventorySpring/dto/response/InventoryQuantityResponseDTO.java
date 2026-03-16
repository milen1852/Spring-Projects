package com.example.inventorySpring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryQuantityResponseDTO {

    private boolean success;

    private String message;

    private Integer remainingQuantity;
}
