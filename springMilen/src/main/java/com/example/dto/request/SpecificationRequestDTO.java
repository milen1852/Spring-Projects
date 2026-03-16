package com.example.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationRequestDTO {

    private Integer page;

    private Integer size;

    private String sortField;

    private String sortDir;

    private Integer prodId;

    private String prodName;

    private String prodCategory;

    private Double minPrice;

    private Double maxPrice;

    private LocalDate orderDateFrom;

    private LocalDate orderDateTo;
}
