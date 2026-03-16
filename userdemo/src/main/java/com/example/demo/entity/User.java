package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User Entity - Represents the "users" table in the database
 * 
 * This is the JPA entity that maps to your database table.
 * Hibernate/JPA will automatically create this table based on the annotations.
 * 
 * Key Annotations:
 * @Entity - Marks this class as a JPA entity
 * @Table - Specifies the table name in the database
 * @Data - Lombok: Generates getters, setters, toString, equals, hashCode
 * @Builder - Lombok: Enables builder pattern for object creation
 * @NoArgsConstructor - Lombok: Generates no-argument constructor (required by JPA)
 * @AllArgsConstructor - Lombok: Generates all-argument constructor
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    /**
     * Primary Key
     * @Id - Marks this field as the primary key
     * @GeneratedValue - Auto-generates the ID value
     * GenerationType.IDENTITY - Database auto-increment strategy
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User's name
     * @Column - Specifies column properties
     * nullable = false - NOT NULL constraint in database
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * User's email - Must be unique
     * unique = true - Creates UNIQUE constraint in database
     */
    @Column(nullable = false, unique = true)
    private String email;
    
    /**
     * User's age
     */
    @Column(nullable = false)
    private Integer age;
    
    /**
     * Timestamp when record was created
     */
    @Column
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when record was last updated
     */
    @Column(insertable = false)
    private LocalDateTime updatedAt;
}