package com.example.inventorySpring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseDTO {

    private Integer productId;

    private Integer warehouseId;

    private Integer quantity;
}
