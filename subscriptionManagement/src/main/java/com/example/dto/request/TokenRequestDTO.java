package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
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
    private String password;
}
