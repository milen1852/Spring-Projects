package com.example.userSpring.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "orderClient",
        url = "http://localhost:8081", fallbackFactory = OrderFallBackFactory.class)

public interface OrderClient {

    @GetMapping("/orders/view/{orderId}")
    boolean checkOrder(@PathVariable Integer orderId);
}
