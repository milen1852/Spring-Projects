package com.example.inventorySpring.controller;

import com.example.inventorySpring.dto.request.InventoryRequestDTO;
import com.example.inventorySpring.dto.request.OrderRequestDTO;
import com.example.inventorySpring.dto.request.UpdateInventoryRequestDTO;
import com.example.inventorySpring.dto.response.InventoryResponseDTO;
import com.example.inventorySpring.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO
                                                                inventoryRequestDTO){
        log.info("POST /inventory/add - Adding a Inventory");

        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory(inventoryRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryResponseDTO);
    }

    @GetMapping("/view")
    public ResponseEntity<List<InventoryResponseDTO>> getInventories(){

        log.info("GET /inventory/view - Fetching All Inventories");

        List<InventoryResponseDTO> inventoryResponseDTO = inventoryService.getInventories();

        if(inventoryResponseDTO != null)
            log.info("Fetched {} inventories", inventoryResponseDTO.size());

        return ResponseEntity.ok(inventoryResponseDTO);
    }

    @GetMapping("/view/{productId}/{warehouseId}")
    public ResponseEntity<InventoryResponseDTO> getInventoryById(@PathVariable Integer productId,
                                                                 @PathVariable Integer warehouseId){
        log.info("GET /view/productId/warehouseId - Fetching Inventory");

        InventoryResponseDTO inventoryResponseDTO = inventoryService.getInventoryById(productId, warehouseId);

        if(inventoryResponseDTO != null)
            log.info("Fetched  inventory with product ID: {}", productId);

        return ResponseEntity.ok(inventoryResponseDTO);
    }

    @PutMapping("/update/{productId}/{warehouseId}")
    public ResponseEntity<InventoryResponseDTO> updateInventoryById(@PathVariable Integer productId,
                                                                    @PathVariable Integer warehouseId, @Valid @RequestBody
                                                                    UpdateInventoryRequestDTO updateInventoryRequestDTO){
        log.info("Update /view/productId/warehouseId - Updating Inventory");

        InventoryResponseDTO inventoryResponseDTO = inventoryService.updateInventoryByID(productId, warehouseId,
                updateInventoryRequestDTO);

        if(inventoryResponseDTO != null)
            log.info("Updated inventory with product ID: {}", productId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(inventoryResponseDTO);
    }

    @DeleteMapping("/delete/{productId}/{warehouseId}")
    public ResponseEntity<InventoryResponseDTO> deleteInventory(@PathVariable Integer productId,
                                                                @PathVariable Integer warehouseId){

        log.info("DELETE /delete/productId/warehouseId - Fetching Inventory");

        InventoryResponseDTO inventoryResponseDTO = inventoryService.deleteInventoryById(productId, warehouseId);

        if(inventoryResponseDTO != null)
            log.info("Deleted inventory with product ID: {}", productId);

        return ResponseEntity.ok(inventoryResponseDTO);
    }

    @GetMapping("/quantity/{productId}")
    public String getTotalQuantity(@PathVariable Integer productId) {
        return "Total Quantity of Product with Id " + productId + " = " +
                inventoryService.calculateTotalProductQuantity(productId);
    }


    //Feign Client Methods


    @GetMapping("/check/{productId}/{warehouseId}")
    public ResponseEntity<Integer> checkStock(@PathVariable Integer productId,
                                                           @PathVariable Integer warehouseId){
        int number = inventoryService.checkStock(productId, warehouseId);
        return ResponseEntity.ok(number);
    }

    @PutMapping("/place-order")
    public ResponseEntity<Integer> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){

        int order = inventoryService.placeOrder(orderRequestDTO);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/update-order")
    public ResponseEntity<Integer> updateOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){

        int order = inventoryService.updateOrder(orderRequestDTO);

        return ResponseEntity.ok(order);
    }

    @PutMapping("/cancel-order")
    public ResponseEntity<Integer> cancelOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){

        return ResponseEntity.status(HttpStatus.OK).body(inventoryService.cancelOrder(orderRequestDTO));
    }
}
