package com.example.controller;

import com.example.dto.request.RefreshTokenRequestDTO;
import com.example.dto.request.TokenRequestDTO;
import com.example.dto.response.IntrospectResponseDTO;
import com.example.dto.response.TokenResponseDTO;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> getAccessToken(@Valid @RequestBody TokenRequestDTO tokenRequestDTO){

        TokenResponseDTO tokenResponseDTO = authService.getAccessToken(tokenRequestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tokenResponseDTO);
    }

    @PostMapping("/token/introspect")
    public ResponseEntity<IntrospectResponseDTO> introspectToken(@RequestParam String token){

        IntrospectResponseDTO introspectResponseDTO = authService.introspectToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(introspectResponseDTO);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseDTO> getRefreshToken(@RequestBody RefreshTokenRequestDTO requestDTO){

        TokenResponseDTO tokenResponseDTO = authService.getRefreshToken(requestDTO);

        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/token/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequestDTO requestDTO){

        authService.logout(requestDTO);

        return ResponseEntity.ok(Map.of("message", "Logged Out Successfully"));
    }
}
