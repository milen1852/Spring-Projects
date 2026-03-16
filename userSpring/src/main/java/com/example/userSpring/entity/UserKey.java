package com.example.userSpring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserKey {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_email")
    private String userEmail;
}
