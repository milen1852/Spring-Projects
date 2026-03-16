package com.example.repository;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.Employee;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EmployeeSpecification {

    public static Specification<Employee> buildFilter(SpecificationRequestDTO spec) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔍 Search (name)
//            if (spec.getSearchName() != null && !spec.getSearchName().isEmpty()) {
//                log.info(spec.getSearchName());
//                String pattern =  spec.getSearchName().toLowerCase() + "%";
//                log.info(pattern);
//                predicates.add(
//                        cb.or(
//                                cb.like(cb.lower(root.get("firstName")), pattern),
//                                cb.like(cb.lower(root.get("lastName")), pattern)
//                        )
//                );
//                log.info(predicates.toString());
//            }
            if(spec.getFirstName() != null){

                String pattern = spec.getFirstName().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("firstName")), pattern));
            }

            if(spec.getLastName() != null){
                String pattern = spec.getLastName().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("lastName")), pattern));
            }

            if(spec.getEmail() != null){
                String pattern = spec.getEmail().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("key").get("email")), pattern));
            }

            // 🏢 Department filter
            if (spec.getDeptId() != null) {
                predicates.add(cb.equal(root.get("deptId"), spec.getDeptId()));
            }

            // 📊 Status filter
            if (spec.getStatus() != null && !spec.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), spec.getStatus()));
            }

            // 💰 Salary range
            if (spec.getMinSalary() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salary"), spec.getMinSalary()));
            }

            if (spec.getMaxSalary() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salary"), spec.getMaxSalary()));
            }

            if (spec.getHireDateFrom() != null && spec.getHireDateTo() != null){
                predicates.add(cb.between(
                        root.get("hireDate"),
                        spec.getHireDateFrom(),
                        spec.getHireDateTo()
                ));
            }
            else if (spec.getHireDateFrom() != null){
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("hireDate"),
                        spec.getHireDateFrom()
                ));
            }
            else if(spec.getHireDateTo() != null){
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("hireDate"),
                        spec.getHireDateTo()
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
