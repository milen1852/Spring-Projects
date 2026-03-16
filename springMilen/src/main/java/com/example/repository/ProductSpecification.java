package com.example.repository;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.Product;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductSpecification {

    public static Specification<Product> buildFilter(SpecificationRequestDTO spec){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(spec.getProdId() != null){

                predicates.add( cb.equal(root.get("key").get("prodId"), spec.getProdId() ));
            }

            if(spec.getProdName() != null){
                String pattern = spec.getProdName().toLowerCase().trim() + "%";

                predicates.add( cb.like(cb.lower(root.get("key").get("prodName")), pattern ));
            }

            if(spec.getProdCategory() != null){
                String pattern = spec.getProdCategory().toUpperCase().trim() + "%";

                predicates.add(cb.like(cb.upper(root.get("prodCategory")), pattern));
            }

            if(spec.getMinPrice() != null){

                predicates.add(cb.greaterThanOrEqualTo(root.get("prodPrice"), spec.getMinPrice() ));
            }

            if(spec.getMaxPrice() != null){

                predicates.add(cb.lessThanOrEqualTo(root.get("prodPrice"), spec.getMaxPrice() ));
            }

            if(spec.getOrderDateFrom() != null && spec.getOrderDateTo() != null){

                predicates.add(cb.between(root.get("orderDate"), spec.getOrderDateFrom(), spec.getOrderDateTo() ));
            }

            if(spec.getOrderDateFrom() != null){

                predicates.add(cb.greaterThanOrEqualTo(root.get("orderDate"), spec.getOrderDateFrom() ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
