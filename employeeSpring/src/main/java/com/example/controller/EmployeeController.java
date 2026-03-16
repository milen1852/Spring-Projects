package com.example.controller;

import com.example.dto.*;
import com.example.exceptions.TokenInvalidException;
import com.example.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/view")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(){
        log.info("Get /employees/view - Fetching all users");

        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();

        log.info("Found {} emloyees", employees.size());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employees);
    }

    @GetMapping("/view/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Integer employeeId){
        log.info("Get /employees/view/{} - Fetching user with this id", employeeId);

        EmployeeResponseDTO employee = employeeService.getEmployee(employeeId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(employee);
    }

    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO requestDTO){
        log.info("Post /employees/add - Creating employee with email {}", requestDTO.getEmail());

        EmployeeResponseDTO employee = employeeService.createEmployee(requestDTO);

        log.info("Employee created successfully with ID: {}", employee.getEmployeeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @PutMapping("/update/{employeeId}/{email}/{department}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Integer employeeId,
                                                              @PathVariable String email,
                                                              @PathVariable String department,
                                                              @Valid @RequestBody EmployeeRequestDTO requestDTO) {

        EmployeeResponseDTO response = employeeService.updateEmployee(employeeId, email, department, requestDTO);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{employeeId}/{email}/{department}")
    public ResponseEntity<EmployeeResponseDTO> deleteEmployee(@PathVariable Integer employeeId,
                                                              @PathVariable String email,
                                                              @PathVariable String department){
        EmployeeResponseDTO employee = employeeService.deleteEmployee(employeeId, email, department);

        return ResponseEntity.ok(employee);
    }


    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> getToken(@RequestBody TokenRequestDTO tokenRequestDTO){

        TokenResponseDTO tokenResponseDTO = employeeService.getAccessToken(tokenRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDTO);
    }

//    @PostMapping("/auth/token")
//    public ResponseEntity<TokenResponseDTO> getAuthToken(@RequestHeader("username") String username,
//                                                     @RequestHeader("password") String password){
//
//        TokenResponseDTO tokenResponseDTO = employeeService.getAuthAccessToken(username, password);
//
//        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDTO);
//    }

    @PostMapping("/token/introspect")
    public ResponseEntity<IntrospectResponseDTO> tokenIntrospect(@RequestParam String token){

        IntrospectResponseDTO introspectResponseDTO = employeeService.tokenIntrospect(token);

        return ResponseEntity.status(HttpStatus.OK).body(introspectResponseDTO);
    }

    @GetMapping("/token/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByIdToken(@PathVariable Integer employeeId,
                                                                    @RequestHeader("Authorization") String auth){

        if(auth == null || !auth.contains("Bearer")){
            throw new TokenInvalidException("Authorization Token does not contain type of token.");
        }

        String token = auth.replace("Bearer ", "");

        return ResponseEntity.status(HttpStatus.OK)
                .body(employeeService.getEmployeeByIdToken(employeeId, token));
    }
}
