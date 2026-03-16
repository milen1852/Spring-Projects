package com.example.orderSpring.feignClient;

import com.example.orderSpring.customException.InsufficientStockException;
import com.example.orderSpring.customException.OrderNotFoundException;
import com.example.orderSpring.dto.OrderRequestDTO;

public class InventoryClientFallBack implements  InventoryClient{
    @Override
    public int checkStock(Integer productId, Integer warehouseId) {
        return 0;
//        throw new InsufficientStockException("Insufficient Stock");
    }

    @Override
    public void placeOrder(OrderRequestDTO orderRequestDTO) {
        throw new RuntimeException("Program Not Compiled Properly");
    }

    @Override
    public void updateOrder(OrderRequestDTO orderRequestDTO) {
        throw new InsufficientStockException("Insufficient Stock");
    }

    @Override
    public void cancelOrder(OrderRequestDTO orderRequestDTO) {
        throw new OrderNotFoundException("Order Not Found to Delete");
    }
}
