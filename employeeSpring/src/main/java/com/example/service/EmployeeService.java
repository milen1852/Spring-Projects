package com.example.service;

import com.example.dto.*;
import com.example.entity.Employee;
import com.example.entity.EmployeeId;
import com.example.exceptions.EmployeeNotFoundException;
import com.example.exceptions.TokenInvalidException;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final RestTemplate restTemplate;

    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees(){
        log.info("Fetching All Employees from the table.");

        List<Employee> employees = employeeRepository.findAll();

        log.info("Returning {} employees", employees.size());

        return employees.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployee(Integer employeeId){
        log.info("Fetching user with id {}", employeeId);

        Employee employee = employeeRepository.findByIdEmployeeId(employeeId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", employeeId);
                    return new RuntimeException("User not found with ID: " + employeeId);
                });

        log.info("Found user with email : {}", employee.getId().getEmail());

        return convertToResponseDTO(employee);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO){
        log.info("Creating new user with email {}", requestDTO.getEmail());

//        if(employeeRepository.existsByIdEmail(requestDTO.getEmail())){
//            log.error("Email already exits : {}", requestDTO.getEmail());
//            throw new RuntimeException("Email already exits : " + requestDTO.getEmail());
//        }
        Employee employee = convertToEntity(requestDTO);

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with email : {}", employee.getId().getEmail());

        return convertToResponseDTO(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO updateEmployee(Integer employeeId, String email, String department, EmployeeRequestDTO requestDTO) {

        EmployeeId id = EmployeeId.builder()
                .employeeId(employeeId)
                .email(email)
                .department(department)
                .build();

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee with ID " + employeeId + " not found"));

        employee.setFirstName(requestDTO.getFirstName());
        employee.setMiddleName(requestDTO.getMiddleName());
        employee.setLastName(requestDTO.getLastName());
        employee.setAge(requestDTO.getAge());
        employee.setPlace(requestDTO.getPlace());
        employee.setPhoneNo(requestDTO.getPhoneNo());
        employee.setDesignation(requestDTO.getDesignation());
        employee.setSalary(requestDTO.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);

        return convertToResponseDTO(updatedEmployee);
    }


    @Transactional
    public EmployeeResponseDTO deleteEmployee(Integer employeeId, String email, String department) {
        EmployeeId id = EmployeeId.builder()
                .employeeId(employeeId)
                .email(email)
                .department(department)
                .build();

        if(!employeeRepository.existsById(id)){
            log.error("ID does not exits");
            throw new RuntimeException("Employee ID does not exits" );
        }
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeRepository.delete(employee);

        return convertToResponseDTO(employee);
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        return EmployeeResponseDTO.builder()
                .employeeId(employee.getId().getEmployeeId())
                .email(employee.getId().getEmail())
                .department(employee.getId().getDepartment())

                .firstName(employee.getFirstName())
                .middleName(employee.getMiddleName())
                .lastName(employee.getLastName())
                .age(employee.getAge())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .place(employee.getPlace())
                .phoneNo(employee.getPhoneNo())
                .build();
    }

    private Employee convertToEntity(EmployeeRequestDTO requestDTO) {
        return Employee.builder().id(EmployeeId.builder()
                .employeeId(requestDTO.getEmployeeId())
                .email(requestDTO.getEmail())
                .department(requestDTO.getDepartment()).build())

                .firstName(requestDTO.getFirstName())
                .middleName(requestDTO.getMiddleName())
                .lastName(requestDTO.getLastName())
                .age(requestDTO.getAge())
                .designation(requestDTO.getDesignation())
                .salary(requestDTO.getSalary())
                .place(requestDTO.getPlace())
                .phoneNo(requestDTO.getPhoneNo())
                .build();
    }

    @Value("${keycloak.server-url}")
    private String keyCloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;


    public TokenResponseDTO getAccessToken(TokenRequestDTO tokenRequestDTO) {

        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        //Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //Body (form-data)
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", tokenRequestDTO.getUsername());
        map.add("password", tokenRequestDTO.getPassword());

        //Wrapping up headers + body
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        //Calling keycloak
        ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(
                tokenUrl, request, TokenResponseDTO.class);

        return response.getBody();
    }

//    public TokenResponseDTO getAuthAccessToken(String username, String password) {
//
//        String tokenUrl = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
//
//        //Headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        //Body (form-data)
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("grant_type", "password");
//        map.add("client_id", clientId);
//        map.add("client_secret", clientSecret);
//        map.add("username", username);
//        map.add("password", password);
//
//        //Wrapping up headers + body
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//
//        //Calling keycloak
//        ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(
//                tokenUrl, request, TokenResponseDTO.class);
//
//        return response.getBody();
//    }

    public IntrospectResponseDTO tokenIntrospect(String token) {

        String url = keyCloakUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("token", token);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String,String>> request =
                new HttpEntity<>(map, headers);

        ResponseEntity<IntrospectResponseDTO> response = restTemplate.postForEntity
                (url, request, IntrospectResponseDTO.class);

        return response.getBody();
    }

    public EmployeeResponseDTO getEmployeeByIdToken(Integer employeeId, String token) {

        IntrospectResponseDTO introspectResponseDTO = tokenIntrospect(token);

        if(!introspectResponseDTO.isActive()){
            throw new TokenInvalidException("Token is Inactive");
        }

        Employee employee = employeeRepository.findByIdEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found with this ID"));

        return convertToResponseDTO(employee);
    }
}
