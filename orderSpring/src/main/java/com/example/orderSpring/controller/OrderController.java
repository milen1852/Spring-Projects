package com.example.orderSpring.controller;

import com.example.orderSpring.dto.GetOrderResponseDTO;
import com.example.orderSpring.dto.OrderRequestDTO;
import com.example.orderSpring.dto.OrderResponseDTO;
import com.example.orderSpring.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/check/{productId}/{warehouseId}")
    public String checkStock(@PathVariable Integer productId, @PathVariable Integer warehouseId){

        return "Total Quantity of Product ID : " + productId + " in Warehouse ID : " + warehouseId + " = "
                + orderService.checkStockService(productId, warehouseId);
    }

    @GetMapping("/view/{orderId}")
    public Boolean checkOrder(@PathVariable Integer orderId){

        return orderService.checkOrderService(orderId);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<GetOrderResponseDTO> getOrder(@PathVariable Integer orderId){

        GetOrderResponseDTO getOrderResponseDTO = orderService.getOrderService(orderId);

        return ResponseEntity.ok(getOrderResponseDTO);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO){

        return ResponseEntity.ok(orderService.placeOrderService(orderRequestDTO));
    }

    @PutMapping("/updateOrder/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Integer orderId,
                                                        @Valid @RequestBody OrderRequestDTO orderRequestDTO){

        OrderResponseDTO orderResponseDTO = orderService.updateOrderService(orderId, orderRequestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(orderResponseDTO);
    }

    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Integer orderId,
                                                        @Valid @RequestBody OrderRequestDTO orderRequestDTO){

        OrderResponseDTO orderResponseDTO = orderService.cancelOrderService(orderId, orderRequestDTO);

        return ResponseEntity.status(HttpStatus.FOUND).body(orderResponseDTO);
    }
}
