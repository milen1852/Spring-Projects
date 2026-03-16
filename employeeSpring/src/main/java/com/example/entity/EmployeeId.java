package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeId implements Serializable {

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "email")
    private String email;

    @Column(name = "department")
    private String department;
}
