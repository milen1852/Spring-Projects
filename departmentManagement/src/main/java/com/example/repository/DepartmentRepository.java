package com.example.repository;

import com.example.entity.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
    boolean existsByName(
            @NotBlank(message = "{name.NotBlank}")
            @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z0-9 ]+$", message = "{name.Pattern}")
            String name);

}
