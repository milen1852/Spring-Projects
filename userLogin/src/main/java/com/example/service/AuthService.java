package com.example.service;

import com.example.dto.response.IntrospectResponseDTO;
import com.example.dto.request.RefreshTokenRequestDTO;
import com.example.dto.request.TokenRequestDTO;
import com.example.dto.response.TokenResponseDTO;
import com.example.entity.Status;
import com.example.entity.User;
import com.example.exceptions.KeycloakException;
import com.example.exceptions.TokenException;
import com.example.exceptions.UserInactiveException;
import com.example.exceptions.UserNotFoundException;
import com.example.repository.UserRepository;
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

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final KeycloakAdminService keycloakAdminService;

    private final RestTemplate restTemplate;

    private final UserLogService userLogService;

    @Value("${keycloak.server-url}")
    private String keyCloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public TokenResponseDTO login(TokenRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUsername()).orElseThrow(() ->
                new UserNotFoundException("User not found with Username : " + requestDTO.getUsername()));

        if(user.getStatus().equals(Status.INACTIVE)){
            userLogService.log(user.getUserId(), "FAILED", "USER IS INACTIVE");
            throw new UserInactiveException("User is Inactive");
        }

        if(!Boolean.TRUE.equals(keycloakAdminService.userExistsInKeycloak(requestDTO.getUsername()))){
            userLogService.log(user.getUserId(), "FAILED", "USER NOT FOUND IN KEYCLOAK");
            throw new KeycloakException("User not found in Keycloak");
        }

        try{
            TokenResponseDTO tokenResponse = getAccessToken(requestDTO);
            if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
                throw new TokenException("Invalid Credentials");
            }

            IntrospectResponseDTO introspectResponse = introspectToken(tokenResponse.getAccessToken());
            if(!introspectResponse.isActive()){
                userLogService.log(user.getUserId(), "FAILED", "TOKEN IS INACTIVE");
                throw new TokenException("Token is Inactive");
            }

            user.setModifiedBy(requestDTO.getUsername());
            user.setModifiedOn(LocalDateTime.now());
            userRepository.save(user);

            userLogService.log(user.getUserId(), "SUCCESS", "LOGIN SUCCESS");

            return tokenResponse;
        }
        catch (Exception e){
            userLogService.log(user.getUserId(), "FAILED", "INVALID CREDENTIALS");
            throw new TokenException("Invalid Credentials");
        }
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

        HttpEntity<MultiValueMap<String, String>> request= new HttpEntity<>(body, httpHeaders);

        ResponseEntity<TokenResponseDTO> tokenResponse = restTemplate
                .postForEntity(tokenUrl, request, TokenResponseDTO.class);

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

        log.info("Introspecting Token");

        return introspectResponse.getBody();
    }

    public TokenResponseDTO refreshToken(RefreshTokenRequestDTO requestDTO){

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

        log.info("Refreshing Token");

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
