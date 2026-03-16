package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestDTO {

    @NotBlank(message = "{name.NotBlank}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{name.Pattern}")
    private String name;

    @Pattern(regexp = "^(?=.[A-Za-z])[A-Za-z0-9 ]+$", message = "{location.Pattern}")
    private String location;
}
