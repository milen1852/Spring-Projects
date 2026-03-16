package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDTO {

    @NotBlank(message = "{username.NotBlank}")
    private String username;

    @NotBlank(message = "{password.NotBlank}")
    @Size(min = 7, message = "{password.Size}")
    private String password;
}
