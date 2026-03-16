package com.example.inventorySpring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponseDTO {

    private Integer warehouseId;

    private String warehouseName;

    private String location;
}
