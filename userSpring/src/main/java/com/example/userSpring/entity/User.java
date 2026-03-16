package com.example.userSpring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @EmbeddedId
    private UserKey key;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_place")
    private String userPlace;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "password")
    private String password;
}
