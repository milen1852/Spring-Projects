package com.example.userSpring.securityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

         httpSecurity.csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/users/view").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated())

                 .sessionManagement(session -> session
                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                 .httpBasic(Customizer.withDefaults());
         return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){                   //For hard-coded username and password
//
//        UserDetails user = User.builder()
//                .username("Milen")
//                .password("1211")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
//        provider.setUserDetailsPasswordService(userDetailsService);
//    }
}
