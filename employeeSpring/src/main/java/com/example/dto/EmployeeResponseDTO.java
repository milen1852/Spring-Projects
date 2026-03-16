package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Integer employeeId;

    private String email;

    private String department;

    private String firstName;

    private String middleName;

    private String lastName;

    private Integer age;

    private String designation;

    private Double salary;

    private String place;

    private String phoneNo;
}
