package com.example.userSpring.feignClient;

import com.example.userSpring.exceptions.OrderServiceUnavailableException;
import org.springframework.cloud.openfeign.FallbackFactory;

public class OrderFallBackFactory implements FallbackFactory<OrderClient> {

    @Override
    public OrderClient create(Throwable cause){

        return orderId -> {
            throw new OrderServiceUnavailableException("Order Service is currently Unavailable");
        };
    }
}
