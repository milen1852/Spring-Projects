package com.example.mapper;

import com.example.dto.request.UpdateEmployeeRequestDTO;
import com.example.entity.Employee;
import org.mapstruct.Mapper;

import java.util.Optional;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    default Employee updateEmployeeMapper(Employee employee, UpdateEmployeeRequestDTO employeeRequestDTO){

        Optional.ofNullable(employeeRequestDTO.getFirstName()).ifPresent(employee::setFirstName);
        Optional.ofNullable(employeeRequestDTO.getLastName()).ifPresent(employee::setLastName);
        Optional.ofNullable(employeeRequestDTO.getPhoneNumber()).ifPresent(employee::setPhoneNumber);
        Optional.ofNullable(employeeRequestDTO.getDeptId()).ifPresent(employee::setDeptId);
        Optional.ofNullable(employeeRequestDTO.getSalary()).ifPresent(employee::setSalary);
        Optional.ofNullable(employeeRequestDTO.getStatus()).ifPresent(employee::setStatus);
        Optional.ofNullable(employeeRequestDTO.getHireDate()).ifPresent(employee::setHireDate);

        return employee;
    }
}
