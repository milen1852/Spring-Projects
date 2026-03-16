package com.example.userSpring.mapper;

import com.example.userSpring.dto.UpdateUserRequestDTO;
import com.example.userSpring.dto.UserResponseDTO;
import com.example.userSpring.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User userConvertToEntityMapper(@MappingTarget User user, UpdateUserRequestDTO updateUserRequestDTO);

    @Mapping(source = "key.userId", target = "userId")
    @Mapping(source = "key.userEmail", target = "userEmail")
    UserResponseDTO userConvertToResponseMapper(User user);
}
