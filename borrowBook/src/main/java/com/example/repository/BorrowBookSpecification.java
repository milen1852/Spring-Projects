package com.example.repository;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.BorrowBooks;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Component
public class BorrowBookSpecification {

    public Specification<BorrowBooks> buildFilter(@RequestBody SpecificationRequestDTO spec){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(spec.getBookId() != null){

                predicates.add(cb.equal(root.get("bookId"), spec.getBookId()));
            }

            if(spec.getBorrowedBy() != null){
                String pattern = spec.getBorrowedBy().toLowerCase().trim() + "%";

                predicates.add(cb.like(cb.lower(root.get("borrowedBy")), pattern));
            }

            if(spec.getStatus() != null){
                String pattern = spec.getStatus().toLowerCase().trim() + "%";

                predicates.add(cb.like(cb.lower(root.get("status")), pattern));
            }

            if(spec.getFromBorrowedDate() != null && spec.getToBorrowedDate() != null){

                predicates.add(cb.between(root.get("borrowDate"),
                        spec.getFromBorrowedDate(), spec.getToBorrowedDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
