package com.example.controller;

import com.example.dto.request.RefreshTokenRequestDTO;
import com.example.dto.response.IntrospectResponseDTO;
import com.example.dto.response.ProductResponseDTO;
import com.example.dto.request.TokenRequestDTO;
import com.example.dto.response.TokenResponseDTO;
import com.example.exceptions.TokenInvalidException;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token/login")
    public ResponseEntity<TokenResponseDTO> getAccessToken(@Valid @RequestBody TokenRequestDTO tokenRequestDTO){

        TokenResponseDTO tokenResponseDTO = authService.getAccessToken(tokenRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDTO);
    }

    @PostMapping("/token/introspect")
    public ResponseEntity<IntrospectResponseDTO> introspectToken(@RequestParam String token){

        IntrospectResponseDTO introspectResponseDTO = authService.introspectToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(introspectResponseDTO);
    }

    @GetMapping("token/view/{prodId}/{prodName}")
    public ResponseEntity<ProductResponseDTO> getTokenById(@PathVariable Integer prodId, @PathVariable String prodName,
                                                           @RequestHeader("Authorization") String auth){
        if(auth == null || !auth.contains("Bearer"))
            throw new TokenInvalidException("Token Provided is Invalid");

        String token = auth.replace("Bearer ", "").trim();

        ProductResponseDTO productResponseDTO = authService.getTokenById(prodId, prodName, token);

        return ResponseEntity.status(HttpStatus.FOUND).body(productResponseDTO);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO requestDTO){

        TokenResponseDTO tokenResponseDTO = authService.refreshToken(requestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDTO);
    }

    @PostMapping("/token/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequestDTO requestDTO){

        authService.logout(requestDTO);

        return ResponseEntity.ok(Map.of("message", "Logged Out Successfully"));
    }
}
