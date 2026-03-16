package com.example.inventorySpring.repository;

import com.example.inventorySpring.entity.Inventory;
import com.example.inventorySpring.entity.InventoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryKey> {

    List<Inventory> findByKeyProductId(Integer productId);

    Optional<Inventory> findByKeyProductIdAndKeyWarehouseId(Integer productId, Integer warehouseId);
}
