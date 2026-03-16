package com.example.service;

import com.example.dto.request.RefreshTokenRequestDTO;
import com.example.dto.response.IntrospectResponseDTO;
import com.example.dto.response.ProductResponseDTO;
import com.example.dto.request.TokenRequestDTO;
import com.example.dto.response.TokenResponseDTO;
import com.example.entity.Product;
import com.example.exceptions.ProductNotFoundException;
import com.example.exceptions.TokenInvalidException;
import com.example.repository.ProductRepository;
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

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final RestTemplate restTemplate;

    @Value("${keycloak.server-url}")
    private String keyCloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public TokenResponseDTO getAccessToken(TokenRequestDTO tokenRequestDTO){

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", tokenRequestDTO.getUsername());
        body.add("password", tokenRequestDTO.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<TokenResponseDTO> tokenResponseDTO =
                restTemplate.postForEntity(tokenUrl, request, TokenResponseDTO.class);

        return tokenResponseDTO.getBody();
    }

    public IntrospectResponseDTO introspectToken(String token) {

        String introspectUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<IntrospectResponseDTO> introspectResponseDTO =
                restTemplate.postForEntity(introspectUrl, request, IntrospectResponseDTO.class);

        return introspectResponseDTO.getBody();
    }

    public ProductResponseDTO getTokenById(Integer prodId,String prodName, String token) {

        IntrospectResponseDTO introspectResponseDTO = introspectToken(token);

        if(!introspectResponseDTO.isActive())
            throw new TokenInvalidException("Token Generated is Expired");

        Product product = productRepository.findByKeyProdIdAndKeyProdName(prodId, prodName).orElseThrow(() ->
                new ProductNotFoundException("Product Not Found with this ID"));

        return productService.convertToResponseDTO(product);
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

        ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(tokenUrl, request, TokenResponseDTO.class);

        return response.getBody();
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
