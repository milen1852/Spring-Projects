package com.example.inventorySpring.controller;

import com.example.inventorySpring.customExceptions.WarehouseNotFoundException;
import com.example.inventorySpring.dto.request.UpdateWarehouseRequestDTO;
import com.example.inventorySpring.dto.request.WarehouseRequestDTO;
import com.example.inventorySpring.dto.response.WarehouseResponseDTO;
import com.example.inventorySpring.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping("/add")
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody
                                                                WarehouseRequestDTO warehouseRequestDTO){
        log.info("POST /warehouse/add - Adding a Warehouse with a specific ID");

        WarehouseResponseDTO warehouseResponseDTO = warehouseService.createWarehouse(warehouseRequestDTO);

        log.info("Created a Warehouse with ID : {}", warehouseResponseDTO.getWarehouseId());

        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseResponseDTO);
    }

    @GetMapping("/view")
    public ResponseEntity<List<WarehouseResponseDTO>> getWarehouses(){

        log.info("GET /warehouse/view - Fetching all warehouses");

        List<WarehouseResponseDTO> warehouseResponseDTO = warehouseService.getWarehouses();

        if(warehouseResponseDTO != null)
            log.info("Fetched {} warehouses", warehouseResponseDTO.size());

        return ResponseEntity.ok(warehouseResponseDTO);
    }

    @GetMapping("/view/{warehouseId}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouseById(@PathVariable Integer warehouseId){

        log.info("GET /warehouse/view/{} - Fetching warehouse with this ID", warehouseId);

        WarehouseResponseDTO warehouseResponseDTO = warehouseService.getWarehouseById(warehouseId);

        if(warehouseResponseDTO != null)
            log.info("Found Warehouse with ID: {}", warehouseId);

        return ResponseEntity.status(HttpStatus.FOUND).body(warehouseResponseDTO);
    }

    @PutMapping("/update/{warehouseId}")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(@PathVariable Integer warehouseId,
                                                                @Valid @RequestBody UpdateWarehouseRequestDTO
                                                                updateWarehouseRequestDTO){
        log.info("PUT /warehouse/update/ - Updating a Product with specific ID");

        WarehouseResponseDTO warehouseResponseDTO = warehouseService.updateWarehouse(warehouseId,
                updateWarehouseRequestDTO);

        if(warehouseResponseDTO != null)
            log.info("Updated Product with ID: {}", warehouseId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(warehouseResponseDTO);
    }

    @DeleteMapping("/delete/{warehouseId}")
    public ResponseEntity<WarehouseResponseDTO> deleteWarehouse(@PathVariable Integer warehouseId){

        log.info("DELETE /delete/{} - Deleting warehouse with this ID", warehouseId);

        WarehouseResponseDTO warehouseResponseDTO = warehouseService.deleteWarehouse(warehouseId);

        log.info("Deleted Warehouse with ID : {}", warehouseId);
        return ResponseEntity.ok(warehouseResponseDTO);
    }

    @DeleteMapping("/delete/name/{warehouseName}")
    public ResponseEntity<String> deleteWarehouseByName(@PathVariable String warehouseName){

        log.info("DELETE /delete/ - Deleting warehouse with Name : {}", warehouseName);

        int rows = warehouseService.deleteWarehouseByName(warehouseName);

        if (rows == 0) {
            throw new WarehouseNotFoundException("Warehouse with this name does not exist");
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body("Warehouse not found with Name : " + warehouseName);
        }

        return ResponseEntity.ok("Deleted Warehouse with Name : " + warehouseName);
    }
}
