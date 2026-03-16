package com.example.service;

import com.example.dto.request.RefreshTokenRequestDTO;
import com.example.dto.request.TokenRequestDTO;
import com.example.dto.response.IntrospectResponseDTO;
import com.example.dto.response.TokenResponseDTO;
import com.example.exception.TokenException;
import com.example.exception.UserException;
import com.example.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RestTemplate restTemplate;

    private final AuthUserRepository authUserRepository;

    @Value("${keycloak.server-url}")
    private String keyCloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public TokenResponseDTO login(TokenRequestDTO tokenRequestDTO) {

        boolean userExist = authUserRepository.existsByUsername(tokenRequestDTO.getUsername());

        if(!userExist)
            throw new UserException("User not authorized to access this application");

        TokenResponseDTO tokenResponse = getAccessToken(tokenRequestDTO);

        if(tokenResponse == null || tokenResponse.getAccessToken() == null)
            throw new TokenException("Invalid Credentials");

        IntrospectResponseDTO introspectResponse = introspectToken(tokenResponse.getAccessToken());

        if(introspectResponse == null || !introspectResponse.isActive())
            throw new TokenException("Token is not Active");

        return tokenResponse;
    }

    public TokenResponseDTO getAccessToken(TokenRequestDTO tokenRequestDTO){

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "password");
        body.add("username", tokenRequestDTO.getUsername());
        body.add("password", tokenRequestDTO.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<TokenResponseDTO> tokenResponse = restTemplate.postForEntity(
                tokenUrl, request, TokenResponseDTO.class);

        return tokenResponse.getBody();
    }

    public IntrospectResponseDTO introspectToken(String accessToken){

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", accessToken);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<IntrospectResponseDTO> introspectResponse = restTemplate.postForEntity(
                tokenUrl, request, IntrospectResponseDTO.class);

        return introspectResponse.getBody();
    }

    public TokenResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO){

        log.info("Refreshing Token");

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", requestDTO.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<TokenResponseDTO> tokenResponse = restTemplate.postForEntity(
                tokenUrl, request, TokenResponseDTO.class);

        return tokenResponse.getBody();
    }

    public void logout(RefreshTokenRequestDTO requestDTO) {

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/logout";

        log.info("Logging Out");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", requestDTO.getRefreshToken());
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        restTemplate.postForEntity(tokenUrl, request, Void.class);
    }
}
