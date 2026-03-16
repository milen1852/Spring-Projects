package com.example.inventorySpring.repository;

import com.example.inventorySpring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.prodPrice > :prodPrice")
    List<Product> findProductsAbovePrice(@Param("prodPrice") Double prodPrice);
}
