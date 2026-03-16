package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecificationRequestDTO {

    private Integer page;

    private Integer size;

    private String sortField;

    private String sortDir;

    private String title;

    private String author;

    private String publisher;
}
