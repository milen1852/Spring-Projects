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

    private Integer bookId;

    private String borrowedBy;

    private String status;

    private LocalDate fromBorrowedDate;

    private LocalDate toBorrowedDate;
}
