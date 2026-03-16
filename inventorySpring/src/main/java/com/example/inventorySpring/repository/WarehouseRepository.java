package com.example.inventorySpring.repository;

import com.example.inventorySpring.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Warehouse w WHERE w.warehouseName = :warehouseName")
    int deleteByName(@Param("warehouseName") String warehouseName);
}
