package com.example.repository;

import com.example.dto.SpecificationRequestDTO;
import com.example.entity.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookSpecification {

    public Specification<Book> buildFilter(SpecificationRequestDTO spec){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(spec.getTitle() != null){
                String pattern = spec.getTitle().toLowerCase().trim() + "%";

                predicates.add(cb.like(cb.lower(root.get("title")), pattern));
            }

            if(spec.getAuthor() != null){
                String pattern = spec.getAuthor().toLowerCase().trim() + "%";

                predicates.add(cb.like(cb.lower(root.get("author")), pattern));
            }

            if(spec.getPublisher() != null){
                String pattern = spec.getPublisher().toLowerCase().trim() + "%";

                predicates.add(cb.like(cb.lower(root.get("publisher")), pattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
