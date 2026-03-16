package com.example.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //Not going by default flow, but going by the flow mentioned here
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws RuntimeException {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/employees/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employees/token/introspect").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth -> auth
                        .jwt(jwt -> {})
                );
        return httpSecurity.build();
    }
}
