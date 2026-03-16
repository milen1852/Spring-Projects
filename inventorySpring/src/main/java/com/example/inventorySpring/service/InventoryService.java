package com.example.inventorySpring.service;

import com.example.inventorySpring.customExceptions.InventoryNotFoundException;
import com.example.inventorySpring.customExceptions.ProductNotFoundException;
import com.example.inventorySpring.customExceptions.WarehouseNotFoundException;
import com.example.inventorySpring.dto.request.InventoryRequestDTO;
import com.example.inventorySpring.dto.request.OrderRequestDTO;
import com.example.inventorySpring.dto.request.UpdateInventoryRequestDTO;
import com.example.inventorySpring.dto.response.InventoryResponseDTO;
import com.example.inventorySpring.entity.Inventory;
import com.example.inventorySpring.entity.InventoryKey;
import com.example.inventorySpring.entity.Product;
import com.example.inventorySpring.entity.Warehouse;
import com.example.inventorySpring.repository.InventoryRepository;
import com.example.inventorySpring.repository.ProductRepository;
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
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProductRepository productRepository;

    private final WarehouseRepository warehouseRepository;

    @Transactional
    public InventoryResponseDTO createInventory(InventoryRequestDTO inventoryRequestDTO){
         Product product = productRepository.findById(inventoryRequestDTO.getProductId()).orElseThrow(() -> {
             log.error("Product with ID {} does not exists", inventoryRequestDTO.getProductId());
             throw new ProductNotFoundException("Product ID does not exists");
         });

        Warehouse warehouse =  warehouseRepository.findById(inventoryRequestDTO.getWarehouseId()).orElseThrow(() -> {
            log.error("Warehouse with ID {} does not exists", inventoryRequestDTO.getWarehouseId());
            throw new WarehouseNotFoundException("Warehouse ID does not exists");
        });


        Inventory inventory = inventoryConvertToEntity(inventoryRequestDTO, product, warehouse);

        Inventory createdInventory = inventoryRepository.save(inventory);

        return inventoryConvertToResponse(createdInventory);
    }

    public List<InventoryResponseDTO> getInventories(){

        List<Inventory> inventories = inventoryRepository.findAll();

        return inventories.stream()
                .map(this::inventoryConvertToResponse)
                .collect(Collectors.toList());
    }

    public InventoryResponseDTO getInventoryById(Integer productId, Integer warehouseId){

        Inventory inventory = inventoryRepository
                .findByKeyProductIdAndKeyWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found to Fetch"));

        return inventoryConvertToResponse(inventory);
    }

    @Transactional
    public InventoryResponseDTO updateInventoryByID(Integer productId, Integer warehouseId,
                                                    UpdateInventoryRequestDTO updateInventoryRequestDTO){
        Inventory inventory = inventoryRepository
                .findByKeyProductIdAndKeyWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found to Update"));

        if(updateInventoryRequestDTO.getQuantity() != null){
            inventory.setQuantity(updateInventoryRequestDTO.getQuantity());
        }

        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryConvertToResponse(savedInventory);
    }

    public InventoryResponseDTO deleteInventoryById(Integer productId, Integer warehouseId){

        Inventory inventory = inventoryRepository
                .findByKeyProductIdAndKeyWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found to Delete"));

        inventoryRepository.delete(inventory);

        return inventoryConvertToResponse(inventory);
    }

    public int calculateTotalProductQuantity(Integer productId){

        List<Inventory> inventories = inventoryRepository.findByKeyProductId(productId);

        if(inventories.isEmpty()){
            throw new ProductNotFoundException("Product with ID " + productId + " does not exits in Inventory Table");
        }

        return inventories.stream()
                .mapToInt(inv -> inv.getQuantity())
                .sum();
    }


    //feign client methods


    public int checkStock(Integer productId, Integer warehouseId){

        Inventory inventory = inventoryRepository.findByKeyProductIdAndKeyWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found"));

        return inventory.getQuantity();
    }

    public Integer placeOrder(OrderRequestDTO orderRequestDTO){

        Inventory inventory = inventoryRepository.findByKeyProductIdAndKeyWarehouseId(
                orderRequestDTO.getProductId(), orderRequestDTO.getWarehouseId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found"));

        inventory.setQuantity(inventory.getQuantity() - orderRequestDTO.getOrderQuantity());

        inventoryRepository.save(inventory);

        return inventory.getQuantity();
    }

    public Integer updateOrder(OrderRequestDTO orderRequestDTO){
        Inventory inventory = inventoryRepository.findByKeyProductIdAndKeyWarehouseId(
                        orderRequestDTO.getProductId(), orderRequestDTO.getWarehouseId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found"));

        inventory.setQuantity(inventory.getQuantity() - orderRequestDTO.getOrderQuantity());

        inventoryRepository.save(inventory);

        return inventory.getQuantity();
    }

    public Integer cancelOrder(OrderRequestDTO orderRequestDTO){
        Inventory inventory = inventoryRepository.findByKeyProductIdAndKeyWarehouseId(
                        orderRequestDTO.getProductId(), orderRequestDTO.getWarehouseId())
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found"));

        inventory.setQuantity(inventory.getQuantity() + orderRequestDTO.getOrderQuantity());

        inventoryRepository.save(inventory);

        return inventory.getQuantity();
    }

    private Inventory inventoryConvertToEntity(InventoryRequestDTO inventoryRequestDTO,
                                               Product product,
                                               Warehouse warehouse){
        return Inventory.builder()
                .key(new InventoryKey())
                .product(product)
                .warehouse(warehouse)
                .quantity(inventoryRequestDTO.getQuantity())
                .build();
    }

    private InventoryResponseDTO inventoryConvertToResponse(Inventory inventory){
        return InventoryResponseDTO.builder()
                .productId(inventory.getKey().getProductId())
                .warehouseId(inventory.getKey().getWarehouseId())

                .quantity(inventory.getQuantity())
                .build();
    }
}
