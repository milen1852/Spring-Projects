package com.example.feignClient;

import com.example.dto.response.DepartmentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "departmentManagement",
        url = "http://localhost:8082/api"
//        configuration = FeignClientConfig.class
)
public interface DepartmentClient {

    @GetMapping("/department/{deptId}")
    DepartmentResponseDTO getDepartmentById(@PathVariable Integer deptId);
}
