package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectResponseDTO {

    private boolean active;

    private String username;

    private String email;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("exp")
    private String expiresIn;
}
