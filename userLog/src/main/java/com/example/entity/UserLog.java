package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLog {

    @Id
    @Column(name = "instruction_id")
    private String instructionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "log_time")
    private LocalDateTime logTime;

    @Column(name = "login_status")
    private String loginStatus;

    @Column(name = "description")
    private String description;
}

