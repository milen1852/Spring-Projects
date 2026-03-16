package com.example.userSpring.securityConfig;

import com.example.userSpring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){

        com.example.userSpring.entity.User user = userRepository.findByUserName(username);

        if(user == null)
            throw new UsernameNotFoundException("User Not Found with this Username");

        if((user.getUserName()).equals("Milen Eldo"))
            return User.builder()
                .username(username)
                .password("{noop}" + user.getPassword())
                .roles("ADMIN")
                .build();

        return User.builder()
                .username(username)
                .password("{noop}" + user.getPassword())
                .roles("USER")
                .build();
    }
}
