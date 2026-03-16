package com.example.repository;

import com.example.entity.Product;
import com.example.entity.ProductKey;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductKey>, JpaSpecificationExecutor {

    Optional<Product> findByKeyProdIdAndKeyProdName(Integer prodId, String prodName);

    boolean existsByKeyProdIdAndKeyProdName(Integer prodId, String prodName);
}
