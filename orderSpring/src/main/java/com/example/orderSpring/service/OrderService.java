package com.example.orderSpring.service;

import com.example.orderSpring.customException.InsufficientStockException;
import com.example.orderSpring.customException.OrderNotFoundException;
import com.example.orderSpring.dto.GetOrderResponseDTO;
import com.example.orderSpring.dto.OrderRequestDTO;
import com.example.orderSpring.dto.OrderResponseDTO;
import com.example.orderSpring.entity.Order;
import com.example.orderSpring.feignClient.InventoryClient;
import com.example.orderSpring.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    public int checkStockService(Integer productId, Integer warehouseId){

        return inventoryClient.checkStock(productId, warehouseId);
    }

    public Boolean checkOrderService(Integer orderId){

        if(orderRepository.existsById(orderId))
            return true;

        return false;

        //return orderRepository.existsById(orderId);
    }

    public GetOrderResponseDTO getOrderService(Integer orderId){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order Not Found for this ID"));

        return convertOrderToResponse(order);
    }

    public OrderResponseDTO placeOrderService(OrderRequestDTO orderRequestDTO){

        int stock = checkStockService(orderRequestDTO.getProductId(), orderRequestDTO.getWarehouseId());

        if (stock < orderRequestDTO.getOrderQuantity())
            throw new InsufficientStockException("Product Stock Not Found");

        Order order = convertOrderToEntity(orderRequestDTO);

        orderRepository.save(order);

        inventoryClient.placeOrder(orderRequestDTO);

        return OrderResponseDTO.builder()
                .productId(orderRequestDTO.getProductId())
                .orderQuantity(orderRequestDTO.getOrderQuantity())
                .status("ORDER PLACED")
                .message("PRODUCT with ID " + orderRequestDTO.getProductId() + " Bought")
                .remainingQuantity(stock - orderRequestDTO.getOrderQuantity())
                .build();
    }

    public OrderResponseDTO updateOrderService(Integer orderId, OrderRequestDTO orderRequestDTO){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order Not Found with this ID"));

        int stock = checkStockService(orderRequestDTO.getProductId(), orderRequestDTO.getWarehouseId());

        if (stock < order.getOrderQuantity() + orderRequestDTO.getOrderQuantity())
            throw new InsufficientStockException("Product Stock Not Found");

        order.setOrderQuantity(order.getOrderQuantity() + orderRequestDTO.getOrderQuantity());
        orderRepository.save(order);

        inventoryClient.updateOrder(orderRequestDTO);

        return OrderResponseDTO.builder()
                .productId(orderRequestDTO.getProductId())
                .orderQuantity(orderRequestDTO.getOrderQuantity())
                .status("ORDER UPDATED")
                .message("PRODUCT with ID " + orderRequestDTO.getProductId() + " Bought")
                .remainingQuantity(stock - orderRequestDTO.getOrderQuantity())
                .build();
    }

    public OrderResponseDTO cancelOrderService(Integer orderId,
                                               OrderRequestDTO orderRequestDTO){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order Not Found with this ID"));

        inventoryClient.cancelOrder(orderRequestDTO);

        order.setOrderQuantity(order.getOrderQuantity() - orderRequestDTO.getOrderQuantity());
        orderRepository.save(order);

        if(order.getOrderQuantity() == 0)
            orderRepository.delete(order);

        return OrderResponseDTO.builder()
                .productId(orderRequestDTO.getProductId())
                .orderQuantity(0)
                .status("ORDER CANCELLED")
                .message("Order with ID " + orderId + " Cancelled")
                .remainingQuantity(checkStockService(orderRequestDTO.getProductId(),
                        orderRequestDTO.getWarehouseId()))
                .build();
    }

    private Order convertOrderToEntity(OrderRequestDTO orderRequestDTO){
        return Order.builder()
                .productId(orderRequestDTO.getProductId())
                .warehouseId(orderRequestDTO.getWarehouseId())
                .orderQuantity(orderRequestDTO.getOrderQuantity())
                .build();
    }

    private GetOrderResponseDTO convertOrderToResponse(Order order){
        return GetOrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderQuantity(order.getOrderQuantity())
                .productId(order.getProductId())
                .warehouseId(order.getWarehouseId())
                .build();
    }
}
