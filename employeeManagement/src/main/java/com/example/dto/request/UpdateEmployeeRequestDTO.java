package com.example.dto.request;

import com.example.entity.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequestDTO {

    @NotBlank(message = "{firstName.NotBlank}")
    @Pattern(regexp = "^$|^[A-Za-z ]+$", message = "{firstName.Pattern}")
    @Size(max = 20, message = "{firstName.Size}")
    private String firstName;

    @NotBlank(message = "{lastName.NotBlank}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{lastName.Pattern}")
    @Size(max = 20, message = "{lastName.Size}")
    private String lastName;

    @Pattern(regexp = "^[0-9]{10}$", message = "{phone.Pattern}")
    private String phoneNumber;

    @NotNull(message = "{deptId.NotNull}")
    @Min(value = 1, message = "{deptId.MinVal}")
    private Integer deptId;

    @DecimalMin(value = "10.0", message = "{salary.MinVal}")
    @Digits(integer = 10, fraction = 2, message = "{salary.Pattern}")
    private Double salary;

    private EmployeeStatus status;

    private LocalDate hireDate;
}
