package com.example.mapper;

import com.example.dto.request.UserRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public User convertToEntity(UserRequestDTO userRequestDTO){

        return User.builder()
                .userId(userRequestDTO.getUserId())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .status(userRequestDTO.getStatus())
                .createdOn(LocalDateTime.now())
                .createdBy(userRequestDTO.getCreatedBy())
                .build();
    }

    public UserResponseDTO convertToResponse(User user){

        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdOn(user.getCreatedOn())
                .createdBy(user.getCreatedBy())
                .modifiedOn(user.getModifiedOn())
                .modifiedBy(user.getModifiedBy())
                .build();
    }
}
