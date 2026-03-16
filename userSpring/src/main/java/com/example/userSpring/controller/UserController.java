package com.example.userSpring.controller;

import com.example.userSpring.dto.GetOrderResponseDTO;
import com.example.userSpring.dto.UpdateUserRequestDTO;
import com.example.userSpring.dto.UserRequestDTO;
import com.example.userSpring.dto.UserResponseDTO;
import com.example.userSpring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/csrf-token")
    public CsrfToken csrf(HttpServletRequest request) {
        log.info("Welcome " + request.getSession().getId());
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO){

        log.info("GET /users/add - Adding a new user");

        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        log.info("Created a new user with email : {}", userRequestDTO.getUserEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @GetMapping("/view")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){

        log.info("GET /users/view - Fetching all Users");

        List<UserResponseDTO> userResponseDTOS = userService.getAllUsers();

        log.info("Fetched {} Users", userResponseDTOS.size());

        return ResponseEntity.ok(userResponseDTOS);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer userId){

        log.info("GET /users/view/{} - Fetching User with provided ID", userId);

        UserResponseDTO userResponseDTO = userService.getUserById(userId);

        log.info("Fetched User with ID : {}", userId);

        return ResponseEntity.status(HttpStatus.FOUND).body(userResponseDTO);
    }

    @PutMapping("/update/{userId}/{userEmail}")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable Integer userId,
                                                          @PathVariable String userEmail,
                                                          @RequestBody @Valid UpdateUserRequestDTO updateUserRequestDTO){

        log.info("UPDATE /users/update/{}/{} - Updating User with provided ID and Email", userId, userEmail);

        UserResponseDTO userResponseDTO = userService.updateUserById(userId, userEmail, updateUserRequestDTO);

        log.info("Updated User with ID : {} and Email : {} ", userId, userEmail);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponseDTO);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserResponseDTO> deleteUserById(@PathVariable Integer userId){

        log.info("DELETE /users/delete/{} - Deleting User with ID", userId);

        UserResponseDTO userResponseDTO = userService.deleteUserById(userId);

        log.info("Deleted User with ID : {} ", userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<GetOrderResponseDTO> getOrderById(@PathVariable Integer orderId){

        GetOrderResponseDTO getOrderResponseDTO = userService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(getOrderResponseDTO);
    }
}
