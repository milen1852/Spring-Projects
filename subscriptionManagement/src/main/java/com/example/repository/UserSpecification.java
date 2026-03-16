package com.example.repository;

import com.example.dto.request.SpecificationRequestDTO;
import com.example.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpecification {

    public Specification<User> buildFilter(SpecificationRequestDTO spec){

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(spec.getUserId() != null){
                predicates.add(cb.equal(root.get("key").get("userId"), spec.getUserId()));
            }

            if(spec.getFirstName() != null){
                String pattern = spec.getFirstName().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("key").get("firstName")), pattern));
            }

            if(spec.getLastName() != null){
                String pattern = spec.getLastName().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("lastName")), pattern));
            }

            if(spec.getEmail() != null){
                String pattern = spec.getEmail().toLowerCase() + "%";

                predicates.add(cb.like(cb.lower(root.get("key").get("email")), pattern));
            }

            if(spec.getPlanType() != null){
                predicates.add(cb.equal(root.get("planType"), spec.getPlanType()));
            }

            if(spec.getPlanId() != null){
                predicates.add(cb.equal(root.get("subscriptionPlan").get("planId"), spec.getPlanId()));
            }

            if(spec.getSubStartDate() != null){
                predicates.add(cb.equal(root.get("subscriptionStartDate"), spec.getSubStartDate()));
            }

            if(spec.getStatus() != null && !spec.getStatus().isEmpty()){
                predicates.add(cb.equal(root.get("subscriptionStatus"), spec.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}