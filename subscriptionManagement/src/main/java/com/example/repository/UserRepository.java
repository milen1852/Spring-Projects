package com.example.repository;

import com.example.entity.User;
import com.example.entity.UserKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UserKey>, JpaSpecificationExecutor<User> {

    boolean existsByKeyUserId(Integer userId);

    boolean existsByKeyEmail(String email);

    Optional<User> findByKeyUserIdAndKeyFirstNameAndKeyEmail(Integer userId, String firstName, String email);

    Optional<User> findByKeyUserId(Integer userId);

}
