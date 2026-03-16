package com.example.repository;

import com.example.entity.Employee;
import com.example.entity.EmployeeKey;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, EmployeeKey>, JpaSpecificationExecutor<Employee> {
    Boolean existsByKeyEmail(
            @NotBlank(message = "{email.NotBlank}")
            @Email(message = "{email.Pattern}")
            String email);

    Optional<Employee> findByKeyEmpId(Integer empId);

    Optional<Employee> findByKeyEmpIdAndKeyEmail(Integer empId, String email);
}
