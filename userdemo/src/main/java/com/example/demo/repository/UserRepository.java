package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository - Data Access Layer
 * 
 * This interface extends JpaRepository which provides:
 * - CRUD operations (save, findById, findAll, delete, etc.)
 * - Pagination and sorting
 * - Query methods by method naming convention
 * 
 * You don't need to write implementation - Spring Data JPA creates
 * the implementation automatically at runtime using proxies.
 * 
 * @Repository - Marks this as a Spring Data repository
 * JpaRepository<User, Long> - User is the entity type, Long is the ID type
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Custom query method to find user by email
     * 
     * Spring Data JPA automatically implements this method by parsing the method name:
     * - "findBy" - indicates a query method
     * - "Email" - property name to search by
     * 
     * Equivalent SQL: SELECT * FROM users WHERE email = ?
     * 
     * @param email - Email to search for
     * @return Optional<User> - May or may not contain a user
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email
     * 
     * Spring Data JPA generates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     * 
     * @param email - Email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
}