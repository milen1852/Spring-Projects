package com.example.service;

import com.example.dto.request.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class KeycloakAdminService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.server-url}")
    private String keyCloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String getAdminToken(){

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(body, httpHeaders);

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, entity, Map.class);
        log.info("Admin Token = {} \n", response != null ? response.toString() : null);

        return response.get("access_token").toString();
    }

    public Boolean userExistsInKeycloak(String username){

        String url = keyCloakUrl + "/admin/realms/" + realm + "/users?username=" + username;

        String token = getAdminToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);

        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(httpHeaders), List.class);

        return response.getBody() != null && !response.getBody().isEmpty();         //Since Boolean Datatype
    }

    public void createUserInKeycloak(UserRequestDTO requestDTO){

        String url = keyCloakUrl + "/admin/realms/" + realm + "/users";

        String token = getAdminToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("username", requestDTO.getUserId());
        body.put("email", requestDTO.getEmail());
        body.put("enabled", true);
        body.put("emailVerified", true);
        body.put("firstName", requestDTO.getFirstName());
        body.put("lastName", requestDTO.getLastName());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

        log.info("User Response {} \n", response);

        String location = response.getHeaders().getLocation().toString();
        log.info("User Location {} \n", location);

        String kcUserId = location.substring(location.lastIndexOf("/") + 1);
        log.info("Keycloak User ID {} \n", kcUserId);
        //extracting uuid for every userId

        setPassword(kcUserId, requestDTO.getPassword(), token);
    }

    public void setPassword(String userId, String password, String token){

        String url = keyCloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("type", "password");
        body.put("temporary", false);
        body.put("value", password);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        restTemplate.put(url, entity);
    }
}
