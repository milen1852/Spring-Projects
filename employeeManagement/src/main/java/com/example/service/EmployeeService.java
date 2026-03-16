package com.example.service;

import com.example.dto.request.EmployeeRequestDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.request.UpdateEmployeeRequestDTO;
import com.example.dto.response.DepartmentResponseDTO;
import com.example.dto.response.EmployeeResponseDTO;
import com.example.entity.Employee;
import com.example.entity.EmployeeKey;
import com.example.entity.EmployeeStatus;
import com.example.exception.EmployeeException;
import com.example.exception.EmployeeNotFoundException;
import com.example.feignClient.DepartmentClient;
import com.example.mapper.EmployeeMapper;
import com.example.repository.EmployeeRepository;
import com.example.repository.EmployeeSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    @Transactional
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO){

        if(Boolean.TRUE.equals(employeeRepository.existsByKeyEmail(employeeRequestDTO.getEmail()))){
            log.error("Employee with email : {} already exists", employeeRequestDTO.getEmail());
            throw new EmployeeException("Employee with email " + employeeRequestDTO.getEmail() + " already exits");
        }

        Employee employee = convertEmployeeToEntity(employeeRequestDTO);

        Employee savedEmployee = employeeRepository.save(employee);

        return convertEmployeeToResponse(savedEmployee);
    }

    public EmployeeResponseDTO getEmployeeById(Integer empId) {

        Employee employee = employeeRepository.findByKeyEmpId(empId).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with ID : " + empId + " does not exists."));

        return convertEmployeeToResponse(employee);
    }

    public List<EmployeeResponseDTO> getEmployees() {

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(this::convertEmployeeToResponse)
                .toList();

    }

    public Page<Employee> getEmployees(SpecificationRequestDTO spec, Pageable pageable) {

        Specification<Employee> specification =
                EmployeeSpecification.buildFilter(spec);

        return employeeRepository.findAll(specification, pageable);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Integer empId, String email, UpdateEmployeeRequestDTO employeeRequestDTO){

        Employee employee = employeeRepository.findByKeyEmpIdAndKeyEmail(empId, email).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with ID : " + empId +
                        " and Email : " + email + " does not exists to update."));

        Employee savedEmployee = employeeMapper.updateEmployeeMapper(employee, employeeRequestDTO);

        employeeRepository.save(savedEmployee);

        return convertEmployeeToResponse(savedEmployee);
    }

    public void softDeleteEmployeeById(Integer empId, String email) {

        Employee employee = employeeRepository.findByKeyEmpIdAndKeyEmail(empId, email).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with ID : " + empId +
                        " and Email : " + email + " does not exists to delete."));

        employee.setStatus(EmployeeStatus.INACTIVE);
        employeeRepository.save(employee);

        convertEmployeeToResponse(employee);
    }


    private Employee convertEmployeeToEntity(EmployeeRequestDTO employeeRequestDTO){
        return Employee.builder()
                .firstName(employeeRequestDTO.getFirstName())
                .lastName(employeeRequestDTO.getLastName())
                .key(EmployeeKey.builder()
                        .email(employeeRequestDTO.getEmail())
                        .empId(employeeRequestDTO.getEmpId())
                        .build())
                .phoneNumber(employeeRequestDTO.getPhoneNumber())
                .deptId(employeeRequestDTO.getDeptId())
                .salary(employeeRequestDTO.getSalary())
                .status(employeeRequestDTO.getStatus())
                .hireDate(employeeRequestDTO.getHireDate())
                .build();
    }

    private EmployeeResponseDTO convertEmployeeToResponse(Employee employee){
        return EmployeeResponseDTO.builder()
                .empId(employee.getKey().getEmpId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getKey().getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .deptId(employee.getDeptId())
                .salary(employee.getSalary())
                .status(employee.getStatus())
                .hireDate(employee.getHireDate())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    private final DepartmentClient departmentClient;

    public EmployeeResponseDTO getEmployeeWithDepartment(Integer empId,String email){

        Employee employee = employeeRepository.findByKeyEmpIdAndKeyEmail(empId, email).orElseThrow(() ->
                new EmployeeNotFoundException("Employee with ID : " + empId +
                        " and Email : " + email + " does not exists."));

        DepartmentResponseDTO departmentResponseDTO = departmentClient.getDepartmentById(employee.getDeptId());

        EmployeeResponseDTO emp = convertEmployeeToResponse(employee);

        emp.setDepartmentResponseDTO(departmentResponseDTO);

        return emp;
    }
}
