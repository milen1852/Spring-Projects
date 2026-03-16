package com.example.inventorySpring.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequestDTO {

    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9 ]+$", message = "Give a valid Product Name")
    private String prodName;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Give a valid Product Category - Only alphabets accepted")
    private String prodCategory;

    @DecimalMin(value = "0.0", inclusive = true, message = "Give a positive Product Price")
    private Double prodPrice;
}
