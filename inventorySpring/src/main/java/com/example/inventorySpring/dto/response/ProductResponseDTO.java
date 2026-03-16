package com.example.inventorySpring.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private Integer productId;

    private String prodName;

    private String prodCategory;

    private Double prodPrice;

    @JsonFormat(pattern = "dd-MM-yyyy HH-mm-ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH-mm-ss")
    private LocalDateTime updatedAt;
}
