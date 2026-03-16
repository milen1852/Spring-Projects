package com.example.repository;

import com.example.entity.Employee;
import com.example.entity.EmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, EmployeeId> {
    Optional<Employee> findByIdEmployeeId(Integer employeeId);

    boolean existsByIdEmail(String email);

    boolean existsByIdEmployeeId(Integer employeeId);

    void deleteById(Integer employeeId);
}
