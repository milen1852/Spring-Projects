package com.example.inventorySpring.service;

import com.example.inventorySpring.customExceptions.WarehouseNotFoundException;
import com.example.inventorySpring.dto.request.UpdateWarehouseRequestDTO;
import com.example.inventorySpring.dto.request.WarehouseRequestDTO;
import com.example.inventorySpring.dto.response.WarehouseResponseDTO;
import com.example.inventorySpring.entity.Warehouse;
import com.example.inventorySpring.mapper.WarehouseMapper;
import com.example.inventorySpring.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    private final WarehouseMapper warehouseMapper;

    @Transactional
    public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO warehouseRequestDTO){

        Warehouse warehouse = warehouseMapper.warehouseConvertToEntity(warehouseRequestDTO);

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        return warehouseMapper.warehouseConvertToResponse(savedWarehouse);
    }

    @Transactional(readOnly = true)
    public List<WarehouseResponseDTO> getWarehouses(){

        List<Warehouse> warehouses = warehouseRepository.findAll();

        return warehouses.stream()
                .map(n -> warehouseMapper.warehouseConvertToResponse(n))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WarehouseResponseDTO getWarehouseById(Integer warehouseId){

        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> {
            log.error("Warehouse with ID: {} does not exists", warehouseId);
            throw new WarehouseNotFoundException("Warehouse ID " + warehouseId + " does not exists");
        });

        return warehouseMapper.warehouseConvertToResponse(warehouse);
    }

    @Transactional
    public WarehouseResponseDTO updateWarehouse(Integer warehouseId,
                                                UpdateWarehouseRequestDTO updateWarehouseRequestDTO){

        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> {
            log.error("Warehouse with ID: {} does not exists to update", warehouseId);
            throw new WarehouseNotFoundException("Warehouse ID " + warehouseId + " does not exists to update");
        });

        if(updateWarehouseRequestDTO.getWarehouseName() != null)
            warehouse.setWarehouseName(updateWarehouseRequestDTO.getWarehouseName());
        if(updateWarehouseRequestDTO.getLocation() != null)
            warehouse.setLocation(updateWarehouseRequestDTO.getLocation());

        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);

        return warehouseMapper.warehouseConvertToResponse(updatedWarehouse);
    }

    public int deleteWarehouseByName(String warehouseName){

        return warehouseRepository.deleteByName(warehouseName);
    }

    public WarehouseResponseDTO deleteWarehouse(Integer warehouseId){

        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> {
            log.error("Warehouse with ID: {} does not exists to delete", warehouseId);
            throw new WarehouseNotFoundException("Warehouse ID " + warehouseId + " does not exists to delete");
        });

        warehouseRepository.delete(warehouse);

        return warehouseMapper.warehouseConvertToResponse(warehouse);
    }
}
