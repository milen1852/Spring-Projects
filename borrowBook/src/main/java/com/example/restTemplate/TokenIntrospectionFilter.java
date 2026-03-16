package com.example.restTemplate;

import com.example.dto.response.IntrospectResponseDTO;
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

    private static final String introspectUrl = "http://localhost:8081/api/token/introspect";

    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

//        String path = request.getRequestURI();
//        if(path.startsWith("/api/token")){
//            chain.doFilter(request, response);
//            return;
//        }

        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer ")){
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = header.substring(7);

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("accessToken", token);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<IntrospectResponseDTO> introspectResponse =
                    restTemplate.exchange(introspectUrl, HttpMethod.POST, entity, IntrospectResponseDTO.class);

            if(introspectResponse.getBody() == null ||
                    !Boolean.TRUE.equals(introspectResponse.getBody().isActive())){
                SecurityContextHolder.clearContext();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    introspectResponse.getBody().getUsername(),
                    token,
                    List.of()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);
        }
        catch (Exception e){
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        }
    }
}
