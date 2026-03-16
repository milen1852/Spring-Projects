package com.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO extends UpdateEmployeeRequestDTO {

    @NotNull(message = "{empId.NotNull}")
    @Min(value = 1, message = "{empId.MinVal}")
    private Integer empId;

    @NotBlank(message = "{email.NotBlank}")
    @Email(message = "{email.Pattern}")
    private String email;
}
