package com.example.inventorySpring.mapper;

import com.example.inventorySpring.dto.request.ProductRequestDTO;
import com.example.inventorySpring.dto.response.ProductResponseDTO;
import com.example.inventorySpring.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product productConvertEntity(ProductRequestDTO productRequestDTO);

    ProductResponseDTO productConvertResponseDTO(Product product);
}
