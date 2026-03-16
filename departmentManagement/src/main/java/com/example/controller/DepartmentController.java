package com.example.controller;

import com.example.dto.request.DepartmentRequestDTO;
import com.example.dto.response.DepartmentResponseDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.Department;
import com.example.service.DepartmentService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping("/department")
    public ResponseEntity<DepartmentResponseDTO> addDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO){

        DepartmentResponseDTO departmentResponseDTO = departmentService.addDepartment(departmentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(departmentResponseDTO);
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Integer deptId){

        DepartmentResponseDTO departmentResponseDTO = departmentService.getDepartmentById(deptId);

        return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDTO);
    }

    @GetMapping("/department")
    public ResponseEntity<List<DepartmentResponseDTO>> getDepartments(){

        List<DepartmentResponseDTO> departmentResponseDTOS = departmentService.getDepartments();

        return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDTOS);
    }

    @PostMapping("/departments")
    public ResponseEntity<Map<String, Object>> getDepartments(@RequestBody SpecificationRequestDTO spec){

        log.info("{}", spec);

        Sort sort = spec.getSortDir().equalsIgnoreCase(spec.getSortDir())
                ? Sort.by(spec.getSortField()).ascending()
                : Sort.by(spec.getSortField()).descending();

        Pageable pageable = PageRequest.of(spec.getPage(), spec.getSize(), sort);

        Page<Department> departmentPage = departmentService.getDepartments(spec, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", departmentPage.getContent());
        response.put("currentPage", departmentPage.getNumber());
        response.put("totalPages", departmentPage.getTotalPages());
        response.put("totalElements", departmentPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/department/{deptId}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Integer deptId,
                                                                  @Valid @RequestBody DepartmentRequestDTO
                                                                          departmentRequestDTO){

        DepartmentResponseDTO departmentResponseDTO = departmentService.updateDepartment(deptId, departmentRequestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(departmentResponseDTO);
    }

    @DeleteMapping("/department/{deptId}")
    public ResponseEntity<DepartmentResponseDTO> deleteDepartment(@PathVariable Integer deptId){

        DepartmentResponseDTO departmentResponseDTO = departmentService.deleteDepartment(deptId);

        return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDTO);
    }

}
