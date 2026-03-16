package com.example.userSpring.service;

import com.example.userSpring.dto.GetOrderResponseDTO;
import com.example.userSpring.dto.UpdateUserRequestDTO;
import com.example.userSpring.dto.UserRequestDTO;
import com.example.userSpring.dto.UserResponseDTO;
import com.example.userSpring.entity.User;
import com.example.userSpring.entity.UserKey;
import com.example.userSpring.exceptions.OrderNotFoundException;
import com.example.userSpring.exceptions.UserExistsException;
import com.example.userSpring.exceptions.UserNotFoundException;
import com.example.userSpring.feignClient.OrderClient;
import com.example.userSpring.mapper.UserMapper;
import com.example.userSpring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final OrderClient orderClient;

    private final RestTemplate restTemplate;

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){

        if(userRepository.existsByKeyUserId(userRequestDTO.getUserId()))
            throw new UserExistsException("User Already exist with User ID :" + userRequestDTO.getUserId());

        if(userRepository.existsByKeyUserEmail(userRequestDTO.getUserEmail()))
            throw new UserExistsException("User Already exist with Email : " + userRequestDTO.getUserEmail());

        if(!orderClient.checkOrder(userRequestDTO.getOrderId()))
            throw new OrderNotFoundException("Order Not Placed for the User");

        User user = convertUserToEntity(userRequestDTO);

        User savedUser = userRepository.save(user);

        return convertUserToResponse(savedUser);
    }

    public List<UserResponseDTO> getAllUsers(){

//        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "key.userId"));
//
//        return users.stream()
//                .map(u -> convertUserToResponse(u))
//                .collect(Collectors.toList());

        List<User> users = userRepository.findAll();

        return users.stream()
                .sorted(Comparator.comparing( (User u) -> u.getKey().getUserId())
                        .thenComparing((User u) -> u.getOrderId() ))
                .map(u -> convertUserToResponse(u))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Integer userId){

        User user = userRepository.findByKeyUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not Found with Id : " + userId));

        return convertUserToResponse(user);
    }

    public UserResponseDTO updateUserById(Integer userId, String userEmail, UpdateUserRequestDTO updateUserRequestDTO){
        User user = userRepository.findByKeyUserIdAndKeyUserEmail(userId, userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id : " + userId));

        if(updateUserRequestDTO.getOrderId() != null && !orderClient.checkOrder(updateUserRequestDTO.getOrderId()))
                throw new OrderNotFoundException("Order Not Found with this ID");


        userMapper.userConvertToEntityMapper(user, updateUserRequestDTO);

        User savedUser = userRepository.save(user);

        return userMapper.userConvertToResponseMapper(savedUser);
    }

    public UserResponseDTO deleteUserById(Integer userId){
        User user = userRepository.findByKeyUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id : " + userId));

        int rows = userRepository.deleteUserById(userId);

        if(rows == 0)
            throw new UserNotFoundException("User Not Found to Delete");

        return userMapper.userConvertToResponseMapper(user);
    }

    private UserResponseDTO convertUserToResponse(User user){

        return UserResponseDTO.builder()
                .userId(user.getKey().getUserId())
                .userEmail(user.getKey().getUserEmail())

                .userName(user.getUserName())
                .userPlace(user.getUserPlace())
                .orderId(user.getOrderId())
                .password(user.getPassword())
                .build();
    }

    private User convertUserToEntity(UserRequestDTO userRequestDTO){

        return User.builder().key(UserKey.builder()
                        .userId(userRequestDTO.getUserId())
                        .userEmail(userRequestDTO.getUserEmail())
                .build())

                .userName(userRequestDTO.getUserName())
                .userPlace(userRequestDTO.getUserPlace())
                .orderId(userRequestDTO.getOrderId())
                .password(userRequestDTO.getPassword())
                .build();
    }


    public GetOrderResponseDTO getOrderById(Integer orderId){

        String url = "http://localhost:8081/orders/" + orderId;
        return restTemplate.getForObject(url, GetOrderResponseDTO.class);
    }
}
