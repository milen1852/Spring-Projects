package com.example.controller;

import com.example.dto.request.UserRequestDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.entity.CreatedBy;
import com.example.entity.Status;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/enum/status")
    public Status[] getStatuses(){
        return Status.values();
    }

    @GetMapping("/user/enum/createdBy")
    public CreatedBy[] getCreatedby(){
        return CreatedBy.values();
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO userResponseDTO = userService.addUser(userRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }
}
