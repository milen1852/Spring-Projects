package com.example.inventorySpring.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarehouseRequestDTO {

    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9 ]+$", message = "Give a valid Warehouse Name")
    private String warehouseName;

    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9 ]+$", message = "Give a valid Location")
    private String location;
}
