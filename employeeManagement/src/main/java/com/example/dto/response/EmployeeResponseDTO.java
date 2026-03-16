package com.example.dto.response;

import com.example.entity.EmployeeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponseDTO {

    private Integer empId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Integer deptId;

    private DepartmentResponseDTO departmentResponseDTO;

    private Double salary;

    private EmployeeStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate hireDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH-mm-ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH-mm-ss")
    private LocalDateTime updatedAt;
}
