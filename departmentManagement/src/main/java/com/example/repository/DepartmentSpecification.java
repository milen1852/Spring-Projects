package com.example.repository;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.Department;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DepartmentSpecification {

    public static Specification<Department> buildFilter(SpecificationRequestDTO spec){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(spec.getDeptName() != null){
                String pattern = spec.getDeptName().toUpperCase() + "%";

                predicates.add(cb.like(cb.upper(root.get("name")), pattern));
            }

            if(spec.getDeptId() != null){
                predicates.add(cb.equal(root.get("deptId"), spec.getDeptId()));
            }

            if(spec.getLocation() != null){
                String pattern = spec.getLocation().toUpperCase() + "%";

                predicates.add(cb.like(cb.upper(root.get("location")), pattern));
            }

            if(spec.getCreatedAtFrom() != null && spec.getCreatedAtTo() != null){
                predicates.add(cb.between(root.get("createdAt"),
                        spec.getCreatedAtFrom(), spec.getCreatedAtTo()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
