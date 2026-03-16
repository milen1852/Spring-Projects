package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service - Business Logic Layer
 * 
 * The service layer:
 * 1. Contains all business logic
 * 2. Coordinates between controller and repository
 * 3. Handles transactions
 * 4. Maps between DTOs and Entities
 * 5. Validates business rules
 * 
 * @Service - Marks this as a Spring service component
 * @RequiredArgsConstructor - Lombok: Generates constructor for final fields
 * @Slf4j - Lombok: Provides logger instance (log.info, log.error, etc.)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    /**
     * Repository dependency injected via constructor
     * 
     * Spring automatically injects UserRepository implementation
     * This is constructor-based dependency injection (preferred over @Autowired)
     */
    private final UserRepository userRepository;
    
    /**
     * Get all users
     * 
     * Flow:
     * 1. Repository fetches all User entities from database
     * 2. Convert each entity to UserResponseDTO
     * 3. Return list of DTOs
     * 
     * @return List of all users as DTOs
     */
    @Transactional(readOnly = true) // Read-only transaction for optimization
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users from database");
        
        // Fetch all entities from database
        List<User> users = userRepository.findAll();
        
        log.info("Found {} users", users.size());
        
        // Convert each entity to DTO using stream and map
        return users.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get user by ID
     * 
     * Flow:
     * 1. Repository queries database for user with given ID
     * 2. If found, convert to DTO and return
     * 3. If not found, throw exception
     * 
     * @param id - User ID to search for
     * @return UserResponseDTO
     * @throws RuntimeException if user not found
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        
        // findById returns Optional<User>
        // orElseThrow() throws exception if user doesn't exist
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new RuntimeException("User not found with ID: " + id);
                });
        
        log.info("Found user: {}", user.getEmail());
        
        return convertToResponseDTO(user);
    }
    
    /**
     * Create new user
     * 
     * Flow:
     * 1. Validate email doesn't already exist
     * 2. Convert DTO to Entity
     * 3. Save entity to database
     * 4. Convert saved entity back to DTO
     * 5. Return DTO to controller
     * 
     * @param requestDTO - User data from client
     * @return Created user as DTO
     * @throws RuntimeException if email already exists
     */
    @Transactional // Wraps method in database transaction
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        log.info("Creating new user with email: {}", requestDTO.getEmail());
        
        // Business rule: Email must be unique
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            log.error("Email already exists: {}", requestDTO.getEmail());
            throw new RuntimeException("Email already exists: " + requestDTO.getEmail());
        }
        
        // Convert DTO to Entity using builder pattern
        User user = convertToEntity(requestDTO);
        
        // Save to database (JPA automatically generates ID and timestamps)
        User savedUser = userRepository.save(user);
        
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        // Convert saved entity back to DTO for response
        return convertToResponseDTO(savedUser);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setAge(requestDTO.getAge());

        User savedUser = userRepository.save(user);

        return convertToResponseDTO(savedUser);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userRepository.delete(user);
    }
    /**
     * Convert Entity to Response DTO
     * 
     * This is manual mapping. Alternatives:
     * - ModelMapper library
     * - MapStruct library (compile-time mapping)
     * 
     * @param user - Entity from database
     * @return UserResponseDTO
     */
    private UserResponseDTO convertToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert Request DTO to Entity
     * 
     * Note: ID and timestamps are NOT set here
     * - ID is auto-generated by database
     * - Timestamps are auto-set by @CreationTimestamp and @UpdateTimestamp
     * 
     * @param requestDTO - DTO from client
     * @return User entity ready to be saved
     */
    private User convertToEntity(UserRequestDTO requestDTO) {
        return User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .age(requestDTO.getAge())
                .build();
    }
}