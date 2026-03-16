package com.example.controller;

import com.example.dto.request.EmployeeRequestDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.request.UpdateEmployeeRequestDTO;
import com.example.dto.response.EmployeeResponseDTO;
import com.example.entity.Employee;
import com.example.entity.EmployeeStatus;
import com.example.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDTO> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){

        EmployeeResponseDTO employeeResponseDTO = employeeService.addEmployee(employeeRequestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employeeResponseDTO);
    }

    @GetMapping("/employee/{empId}/{email}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Integer empId, @PathVariable String email){

        EmployeeResponseDTO employeeResponseDTO = employeeService.getEmployeeWithDepartment(empId, email);

        return ResponseEntity.ok(employeeResponseDTO);
    }

    @GetMapping("/employee")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(){

        List<EmployeeResponseDTO> employeeResponseDTOS = employeeService.getEmployees();

        return ResponseEntity.ok(employeeResponseDTOS);
    }


    @PostMapping("/employees")
    public ResponseEntity<Map<String, Object>> getEmployees(@RequestBody SpecificationRequestDTO spec) {

        log.info("{}",spec);

        Sort sort = spec.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(spec.getSortField()).descending()
                : Sort.by(spec.getSortField()).ascending();

        Pageable pageable = PageRequest.of(spec.getPage(), spec.getSize(), sort);

        Page<Employee> employeePage = employeeService.getEmployees(spec, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", employeePage.getContent());
        response.put("currentPage", employeePage.getNumber());
        response.put("totalPages", employeePage.getTotalPages());
        response.put("totalElements", employeePage.getTotalElements());

        return ResponseEntity.ok(response);
    }


    @PutMapping("/employee/{empId}/{email}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Integer empId, @PathVariable String email,
                                                              @Valid @RequestBody UpdateEmployeeRequestDTO
                                                                      employeeRequestDTO){

        EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployee(empId, email, employeeRequestDTO);

        return new ResponseEntity<>(employeeResponseDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/employee/{empId}/{email}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer empId, @PathVariable String email){

        employeeService.softDeleteEmployeeById(empId, email);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/meta/status")
    public List<String> getStatuses() {
        return Arrays.stream(EmployeeStatus.values())
                .map(Enum::name)
                .toList();
    }
}
