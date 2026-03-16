package com.example.userSpring.repository;

import com.example.userSpring.entity.User;
import com.example.userSpring.entity.UserKey;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserKey> {

    boolean existsByKeyUserEmail(@NotBlank(message = "{userEmail.Notnull}")
                                 @Email(message = "{userEmail.Validation}")
                                 String userEmail);

    boolean existsByKeyUserId(@NotNull(message = "{userId.Notnull}")
                              @Min(value = 1, message = "{userId.min}")
                              @Max(value = 50, message = "{userId.max}")
                              Integer userId);

    Optional<User> findByKeyUserId(Integer userId);

    Optional<User> findByKeyUserIdAndKeyUserEmail(Integer userId, String userEmail);


    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.key.userId = :userId")
    int deleteUserById(@Param("userId") Integer userId);

    User findByUserName(String userName);
}
