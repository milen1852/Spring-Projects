package com.example.restTemplate;

import com.example.dto.IntrospectResponseDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenIntrospectionFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;

    private static final String introspectUrl = "http://localhost:8081/api/token/introspect";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = header.substring(7);

        try {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("accessToken", token);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, httpHeaders);

            ResponseEntity<IntrospectResponseDTO> introspectResponse =
                    restTemplate.exchange(introspectUrl, HttpMethod.POST, entity, IntrospectResponseDTO.class);

            if(introspectResponse == null || !Boolean.TRUE.equals(introspectResponse.getBody().isActive())){
                SecurityContextHolder.clearContext();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    introspectResponse.getBody().getUsername(),
                    null,
                    List.of()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        }
    }
}
