package com.example.orderSpring.feignClient;

import com.example.orderSpring.dto.OrderRequestDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventoryClient",
        url = "http://localhost:8082",
        fallbackFactory = InventoryClientFallBack.class)

public interface InventoryClient {

    @GetMapping("/inventory/check/{productId}/{warehouseId}")
    int checkStock(@PathVariable Integer productId, @PathVariable Integer warehouseId);

    @PutMapping("/inventory/place-order")
    void placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO);

    @PutMapping("/inventory/update-order")
    void updateOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO);

    @PutMapping("/inventory/cancel-order")
    void cancelOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO);
}
