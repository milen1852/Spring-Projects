package com.example.inventorySpring.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequestDTO {

    @NotNull(message = "Product ID required")
    @Min(value = 1, message = "Provide a valid Product ID")
    private Integer productId;

    @NotNull(message = "Warehouse ID required")
    @Min(value = 1, message = "Provide a valid Warehouse ID")
    private Integer warehouseId;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Provide a valid quantity")
    private Integer quantity;
}
