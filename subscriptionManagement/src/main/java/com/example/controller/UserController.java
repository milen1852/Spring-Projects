package com.example.controller;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.request.UserRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.entity.PlanType;
import com.example.entity.SubscriptionStatus;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO userResponseDTO = userService.addUser(userRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @GetMapping("/user/enum/planType")
    public PlanType[] getPlanTypes(){

        return PlanType.values();
    }

    @GetMapping("/user/enum/status")
    public SubscriptionStatus[] getStatus(){

        return SubscriptionStatus.values();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Integer userId){

        UserResponseDTO userResponse = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers(@RequestBody SpecificationRequestDTO spec){

        log.info("{}", spec);

        Map<String, Object> response = userService.getAllUsers(spec);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/user/{userId}/{firstName}/{email}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer userId,@PathVariable String firstName,
                                                      @PathVariable String email, @Valid @RequestBody
                                                      UserRequestDTO requestDTO){

        UserResponseDTO responseDTO = userService.updateUser(userId, firstName, email, requestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
    }

    @DeleteMapping("/user/{userId}/{firstName}/{email}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable Integer userId,@PathVariable String firstName,
                                               @PathVariable String email){

        userService.softDeleteUser(userId, firstName, email);

        return ResponseEntity.noContent().build();
    }
}
