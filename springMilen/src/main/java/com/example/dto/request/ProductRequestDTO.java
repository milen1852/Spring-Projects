package com.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotNull(message = "{prodId.NotBlank}")
    @Min(value = 1, message = "{prodId.MinVal}")
    private Integer prodId;

    @NotBlank(message = "{name.NotBlank}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{name.Pattern}")
    private String prodName;

    @NotBlank(message = "{category.NotBlank}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{category.Pattern}")
    private String prodCategory;

    @DecimalMin(value = "0.0", inclusive = false, message = "{price.MinVal}")
    private Double prodPrice;

    @NotNull(message = "{quantity.NotBlank}")
    @Min(value = 1, message = "{quantity.MinVal}")
    private Integer prodQuantity;

    private LocalDate orderDate;
}
