package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationRequestDTO {

    private Integer page;

    private Integer size;

    private String sortField;

    private String sortDir;

    private Integer userId;

    private String firstName;

    private String lastName;

    private String email;

    private String planType;

    private Integer planId;

    private LocalDate subStartDate;

    private String status;
}
