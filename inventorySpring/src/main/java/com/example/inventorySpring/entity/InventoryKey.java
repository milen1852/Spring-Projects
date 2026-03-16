package com.example.inventorySpring.entity;

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
public class InventoryKey implements Serializable {

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "warehouse_id")
    private Integer warehouseId;
}
