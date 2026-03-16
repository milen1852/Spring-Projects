package com.example.orderSpring.dto;

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
public class OrderRequestDTO {

    @NotNull(message = "{productId.Notnull}")
    @Min(value = 1, message = "{productId.min}")
    private Integer productId;

    @NotNull(message = "{warehouseId.Notnull}")
    @Min(value = 1, message = "{warehouseId.min}")
    private Integer warehouseId;

    @NotNull(message = "{orderQuantity.Notnull}")
    @Min(value = 1, message = "{orderQuantity.min}")
    private Integer orderQuantity;
}
