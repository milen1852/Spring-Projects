package com.example.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

//    @JsonAlias("search")

    private String firstName;

    private String lastName;

    private  String email;

    private Integer deptId;

    private String status;

    private Double minSalary;

    private Double maxSalary;

    private String sortField;

    private String sortDir;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate hireDateFrom;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate hireDateTo;
}
