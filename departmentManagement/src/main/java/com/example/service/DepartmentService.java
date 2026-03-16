package com.example.service;

import com.example.dto.request.DepartmentRequestDTO;
import com.example.dto.response.DepartmentResponseDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.Department;
import com.example.exceptions.DepartmentNameException;
import com.example.exceptions.DepartmentNotFoundException;
import com.example.repository.DepartmentRepository;
import com.example.repository.DepartmentSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO departmentRequestDTO){

        if(departmentRepository.existsByName(departmentRequestDTO.getName())){
            log.error("Department with name : \"{}\" already exists", departmentRequestDTO.getName());
            throw new DepartmentNameException("Department with Name " + departmentRequestDTO.getName() + " Already Exits");
        }

        Department department = convertDepartmentToEntity(departmentRequestDTO);
        department.setCreatedAt(LocalDateTime.now());

        Department savedDepartment = departmentRepository.save(department);

        return convertDepartmentToResponse(savedDepartment);

    }

    public DepartmentResponseDTO getDepartmentById(Integer deptId){

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + deptId +
                        " does not exists to update.")
                );

        return convertDepartmentToResponse(department);
    }

    public List<DepartmentResponseDTO> getDepartments(){

        List<Department> departments = departmentRepository.findAll();

        return departments.stream().
                map(this::convertDepartmentToResponse)
                .collect(Collectors.toList());
    }

    public Page<Department> getDepartments(SpecificationRequestDTO spec, Pageable pageable){

        Specification<Department> specification = DepartmentSpecification.buildFilter(spec);

        return departmentRepository.findAll(specification, pageable);
    }

    @Transactional
    public DepartmentResponseDTO updateDepartment(Integer deptId, DepartmentRequestDTO departmentRequestDTO){

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + deptId +
                        " does not exists to update.")
                );
        department.setName(departmentRequestDTO.getName());
        department.setLocation(departmentRequestDTO.getLocation());

        department.setUpdatedAt(LocalDateTime.now());

        Department updatedDepartment = departmentRepository.save(department);

        return convertDepartmentToResponse(updatedDepartment);
    }

    public DepartmentResponseDTO deleteDepartment(Integer deptId){

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + deptId +
                        " does not exists to delete.")
                );

        departmentRepository.delete(department);

        return convertDepartmentToResponse(department);
    }


    private DepartmentResponseDTO convertDepartmentToResponse(Department department){

        return DepartmentResponseDTO.builder()
                .deptId(department.getDeptId())
                .name(department.getName())
                .location(department.getLocation())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    private Department convertDepartmentToEntity(DepartmentRequestDTO departmentRequestDTO){
        return Department.builder()
                .name(departmentRequestDTO.getName())
                .location(departmentRequestDTO.getLocation())
                .build();
    }
}
