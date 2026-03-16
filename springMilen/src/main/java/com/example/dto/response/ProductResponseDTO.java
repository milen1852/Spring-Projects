package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private Integer prodId;

    private String prodName;

    private String prodCategory;

    private Double prodPrice;

    private Integer prodQuantity;

//    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate orderDate;
}
