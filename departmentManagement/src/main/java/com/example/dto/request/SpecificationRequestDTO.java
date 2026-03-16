package com.example.dto.request;

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
public class SpecificationRequestDTO {

    private Integer page;

    private Integer size;

    private Integer deptId;

    private String deptName;

    private String location;

    @JsonFormat(pattern = "dd/MM/yyyy, HH:mm:ss")
    private LocalDateTime createdAtFrom;

    @JsonFormat(pattern = "dd/MM/yyyy, HH:mm:ss")
    private LocalDateTime createdAtTo;

    private String sortField;

    private String sortDir;
}
