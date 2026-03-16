package com.example.dto.response;

import com.example.entity.CreatedBy;
import com.example.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private Status status;

    private CreatedBy createdBy;

    private LocalDateTime createdOn;

    private String modifiedBy;

    private LocalDateTime modifiedOn;
}
