package com.example.inventorySpring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @EmbeddedId
    private InventoryKey key;

    @ManyToOne                        //Because many inventory entries can reference the same product
    @MapsId("productId")              //This tells Hibernate to link the FK column
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "quantity")
    private Integer quantity;
}
