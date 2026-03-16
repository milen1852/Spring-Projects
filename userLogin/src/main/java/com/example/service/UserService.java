package com.example.service;

import com.example.dto.request.UserRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.entity.User;
import com.example.exceptions.KeycloakException;
import com.example.exceptions.UserException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final KeycloakAdminService keycloakAdminService;

    public UserResponseDTO addUser(UserRequestDTO requestDTO){

        if(userRepository.existsById(requestDTO.getUserId()))
            throw new UserException("User Already Exists in DB");

        if(Boolean.TRUE.equals(keycloakAdminService.userExistsInKeycloak(requestDTO.getUserId())))
            throw new KeycloakException("User Already Exists in Keycloak");

        keycloakAdminService.createUserInKeycloak(requestDTO);

        User user = userMapper.convertToEntity(requestDTO);

        userRepository.save(user);

        return userMapper.convertToResponse(user);
    }
}
