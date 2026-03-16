package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @EmbeddedId
    private ProductKey key;

    @Column(name = "prod_category")
    private String prodCategory;

    @Column(name = "prod_price")
    private Double prodPrice;

    @Column(name = "prod_quantity")
    private Integer prodQuantity;

    @Column(name = "order_date")
    private LocalDate orderDate;
}
