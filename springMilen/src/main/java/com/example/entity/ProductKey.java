package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductKey implements Serializable {

    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "prod_name")
    private String prodName;
}
