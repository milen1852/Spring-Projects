package com.example.dto.request;

import com.example.entity.CreatedBy;
import com.example.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "{userId.NotBlank}")
    private String userId;

    @NotBlank(message = "{password.NotBlank}")
    @Size(min = 7, message = "{password.Size}")
    private String password;

    @NotBlank(message = "{firstName.NotBlank}")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "{firstName.Pattern}")
    private String firstName;

    @NotBlank(message = "{lastName.NotBlank}")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "{lastName.Pattern}")
    private String lastName;

    @NotBlank(message = "{email.NotBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email.Pattern}")
    private String email;

    private Status status;

    private CreatedBy createdBy;
}
