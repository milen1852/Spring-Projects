package com.example.demo.controller;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller - REST API Endpoints
 * 
 * The controller layer:
 * 1. Handles HTTP requests and responses
 * 2. Validates request data
 * 3. Delegates business logic to service layer
 * 4. Returns appropriate HTTP status codes
 * 5. Formats responses as JSON
 * 
 * @RestController - Combines @Controller and @ResponseBody
 *                   All methods return JSON by default
 * @RequestMapping - Base URL path for all endpoints in this controller
 * @RequiredArgsConstructor - Lombok: Constructor injection for final fields
 * @Slf4j - Lombok: Provides logger for logging
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    /**
     * Service dependency injected via constructor
     * 
     * Spring's IoC container automatically provides UserService instance
     * This is dependency injection through constructor (best practice)
     */
    private final UserService userService;

    /**
     * GET endpoint to fetch all users
     * 
     * URL: GET http://localhost:8080/api/users
     * 
     * Request Flow:
     * 1. Client sends GET request
     * 2. DispatcherServlet receives request
     * 3. HandlerMapping maps URL to this method
     * 4. This method is invoked
     * 5. Service layer is called
     * 6. Response is converted to JSON
     * 7. JSON is sent back to client
     * 
     * @return ResponseEntity with list of users and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("GET /api/users - Fetching all users");
        
        // Delegate to service layer
        List<UserResponseDTO> users = userService.getAllUsers();
        
        log.info("Returning {} users", users.size());
        
        // ResponseEntity allows us to set HTTP status code and headers
        // HttpStatus.OK = 200
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET endpoint to fetch user by ID
     * 
     * URL: GET http://localhost:8080/api/users/1
     * 
     * @PathVariable - Extracts {id} from URL path
     * 
     * Example: /api/users/5 -> id = 5
     * 
     * @param id - User ID from URL path
     * @return ResponseEntity with user data and HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user by ID", id);
        
        // Service handles the business logic
        UserResponseDTO user = userService.getUserById(id);
        
        log.info("Returning user: {}", user.getEmail());
        
        return ResponseEntity.ok(user);
    }
    
    /**
     * POST endpoint to create new user
     * 
     * URL: POST http://localhost:8080/api/users
     * Body: { "name": "John", "email": "john@example.com", "age": 25 }
     * 
     * @RequestBody - Deserializes JSON request body to UserRequestDTO
     * @Valid - Triggers validation annotations in DTO (@NotBlank, @Email, etc.)
     * 
     * Request Flow:
     * 1. Client sends POST request with JSON body
     * 2. Jackson (JSON library) converts JSON to UserRequestDTO
     * 3. @Valid triggers validation
     * 4. If validation passes, method is executed
     * 5. If validation fails, Spring returns 400 Bad Request automatically
     * 6. Service creates user in database
     * 7. Response DTO is converted back to JSON
     * 8. JSON sent to client with 201 Created status
     * 
     * @param requestDTO - User data from request body
     * @return ResponseEntity with created user and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO requestDTO) {
        
        log.info("POST /api/users - Creating user with email: {}", 
                requestDTO.getEmail());
        
        // Service handles business logic and database operation
        UserResponseDTO createdUser = userService.createUser(requestDTO);
        
        log.info("User created successfully with ID: {}", createdUser.getId());
        
        // Return 201 CREATED status with created user data
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO requestDTO) {

        UserResponseDTO updatedUser = userService.updateUser(id, requestDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully with ID: " + id);
    }
}