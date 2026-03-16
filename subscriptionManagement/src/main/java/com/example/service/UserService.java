package com.example.service;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.request.UserRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.entity.SubscriptionPlan;
import com.example.entity.SubscriptionStatus;
import com.example.entity.User;
import com.example.exception.PlanTypeException;
import com.example.exception.UserEmailException;
import com.example.exception.UserException;
import com.example.mapper.UserMapper;
import com.example.repository.SubscriptionPlanRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final SubscriptionPlanRepository planRepository;

    private final UserMapper userMapper;

    private final UserSpecification userSpecification;

    @Transactional
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {

        Integer userId = userRequestDTO.getUserId();
        String email = userRequestDTO.getEmail();

        SubscriptionPlan plan = planRepository.findById(userRequestDTO.getPlanId()).orElseThrow(() ->
                new PlanTypeException("Plan with ID : " + userRequestDTO.getPlanId() + " does not exists"));

        if(userRepository.existsByKeyUserId(userId))
            throw new UserException("User with ID : " + userId + " already exits.");

        if(userRepository.existsByKeyEmail(email))
            throw new UserEmailException("User with Email : " + email + " already exits.");

        userRequestDTO.setSubscriptionStartDate(LocalDate.now());
        userRequestDTO.setSubscriptionEndDate(LocalDate.now().plusDays(plan.getDurationDays()));

        User user = userMapper.convertUserToEntity(userRequestDTO);

        User savedUser = userRepository.save(user);

        return userMapper.convertUserToResponse(savedUser);
    }

    public UserResponseDTO getUser(Integer userId) {

        User user = userRepository.findByKeyUserId(userId).orElseThrow(() ->
                new UserException("User with ID : " + userId + " does not exits."));

        return userMapper.convertUserToResponse(user);
    }

    public Map<String, Object> getAllUsers(SpecificationRequestDTO spec){

        Sort sort = spec.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(spec.getSortField()).descending()
                : Sort.by(spec.getSortField()).ascending();

        Pageable pageable = PageRequest.of(spec.getPage(), spec.getSize(), sort);

        Specification<User> specification = userSpecification.buildFilter(spec);

        Page<User> users = userRepository.findAll(specification, pageable);

        Page<UserResponseDTO> userPage = users.map(userMapper::convertUserToResponse);

        Map<String, Object> response = new HashMap<>();

        response.put("content", userPage.getContent());
        response.put("currentPage", userPage.getNumber());
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalElements", userPage.getTotalElements());

        return response;
    }

    public UserResponseDTO updateUser(Integer userId, String firstName, String email, @Valid UserRequestDTO requestDTO) {

        User user = userRepository.findByKeyUserIdAndKeyFirstNameAndKeyEmail(userId, firstName, email).orElseThrow(() ->
                new UserException("User Not Found"));

        user.setLastName(requestDTO.getLastName());
        user.setPhoneNumber(requestDTO.getPhoneNumber());
        user.setPlanType(requestDTO.getPlanType());

        SubscriptionPlan plan = planRepository.findById(requestDTO.getPlanId()).orElseThrow(() ->
                new PlanTypeException("Plan ID NOT Found"));
        user.setSubscriptionPlan(plan);

        user.setPrice(requestDTO.getPrice());
        user.setSubscriptionStartDate(requestDTO.getSubscriptionStartDate());
        user.setSubscriptionEndDate(requestDTO.getSubscriptionEndDate());
        user.setSubscriptionStatus(SubscriptionStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return userMapper.convertUserToResponse(savedUser);
    }

    public void softDeleteUser(Integer userId, String firstName, String email){
        User user = userRepository.findByKeyUserIdAndKeyFirstNameAndKeyEmail(userId, firstName, email).orElseThrow(() ->
                new UserException("User Not Found"));

        user.setIsActive(false);
        user.setSubscriptionStatus(SubscriptionStatus.INACTIVE);

        userRepository.save(user);

        userMapper.convertUserToResponse(user);
    }
}
