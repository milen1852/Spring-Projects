package com.example.inventorySpring.mapper;

import com.example.inventorySpring.dto.request.WarehouseRequestDTO;
import com.example.inventorySpring.dto.response.WarehouseResponseDTO;
import com.example.inventorySpring.entity.Warehouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    Warehouse warehouseConvertToEntity(WarehouseRequestDTO warehouseRequestDTO);

    WarehouseResponseDTO warehouseConvertToResponse(Warehouse warehouse);
}
