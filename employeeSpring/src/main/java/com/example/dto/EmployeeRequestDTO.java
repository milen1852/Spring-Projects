package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

    @NotNull(message = "Employee Id is required.")
    private Integer employeeId;

    @NotBlank(message = "First Name is required.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "First name must only contain alphabets.")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters.")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Middle name must only contain alphabets.")
    private String middleName;

    @NotBlank(message = "Last Name is required.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Last name must only contain alphabets.")
    @Size(min = 1, max = 10, message = "Name must be between 1 and 10 characters.")
    private String lastName;

    @Min(value = 10, message = "Age must be atleast 10 years.")
    @Max(value = 120, message = "Age must be atmost 120 years.")
    private Integer age;

    @NotBlank(message = "Designation is Required.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Designation must only contain alphabets.")
    private String designation;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be a positive number.")
    private Double salary;

    @Pattern(regexp = "^[A-Za-z ]+$", message = "Place Name must only contain alphabets.")
    private String place;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone Number should only contain 10 digits.")
    private String phoneNo;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Department is Required.")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Department must only contain alphabets.")
    private String department;
}
