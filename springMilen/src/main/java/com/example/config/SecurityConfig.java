package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(customizer -> customizer.disable())

                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/token/**").permitAll()

//                        .requestMatchers(HttpMethod.GET, "/products/view").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/products/add").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/products/update/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .oauth2ResourceServer(auth -> auth
                        .jwt(jwt -> {}));

//        http.formLogin(Customizer.withDefaults());
//        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
//    @Bean
//    public JwtAuthenticationConverter jwtAuthConverter() {
//        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//        converter.setAuthorityPrefix("ROLE_");
//        converter.setAuthoritiesClaimName("realm_access.roles");
//
//        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
//        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
//        return jwtConverter;
//    }
//
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }
//
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails user1 = User.withDefaultPasswordEncoder()
//                .username("Milen")
//                .password("1211")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user2 = User.withDefaultPasswordEncoder()
//                .username("Mithul")
//                .password("2603")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);  // n number of users can be used
//    }
}
