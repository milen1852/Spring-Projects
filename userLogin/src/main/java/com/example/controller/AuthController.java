package com.example.controller;

import com.example.dto.request.IntrospectRequestDTO;
import com.example.dto.request.RefreshTokenRequestDTO;
import com.example.dto.request.TokenRequestDTO;
import com.example.dto.response.IntrospectResponseDTO;
import com.example.dto.response.TokenResponseDTO;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody TokenRequestDTO requestDTO){

        TokenResponseDTO tokenResponse = authService.login(requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponseDTO> introspectToken(@RequestBody IntrospectRequestDTO requestDTO){

        IntrospectResponseDTO introspectResponse = authService.introspectToken(requestDTO.getAccessToken());

        return ResponseEntity.status(HttpStatus.OK).body(introspectResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO requestDTO){

        TokenResponseDTO tokenResponse = authService.refreshToken(requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequestDTO requestDTO){

        authService.logout(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logged Out Successfully"));
    }
}
