package com.example.inventorySpring.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryRequestDTO {

    @Min(value = 0, message = "Provide a valid quantity")
    private Integer quantity;
}
