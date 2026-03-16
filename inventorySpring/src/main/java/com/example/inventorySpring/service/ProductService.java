package com.example.inventorySpring.service;

import com.example.inventorySpring.customExceptions.ProductCategoryException;
import com.example.inventorySpring.customExceptions.ProductNotFoundException;
import com.example.inventorySpring.dto.request.ProductRequestDTO;
import com.example.inventorySpring.dto.request.UpdateProductRequestDTO;
import com.example.inventorySpring.dto.response.ProductResponseDTO;
import com.example.inventorySpring.entity.Product;
import com.example.inventorySpring.mapper.ProductMapper;
import com.example.inventorySpring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO){

        Product product = productMapper.productConvertEntity(productRequestDTO);

        product.setCreatedAt(LocalDateTime.now());
        Product createdProduct = productRepository.save(product);

        return productMapper.productConvertResponseDTO(createdProduct);
    }

    public List<ProductResponseDTO> getProducts(){

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(productMapper::productConvertResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductByID(Integer productId){

        Product product = productRepository.findById(productId).orElseThrow(() -> {
                    log.error("Product with ID: {} does not exists", productId);
                    throw new ProductNotFoundException("Product with ID : " + productId + " does not exist ");
                });

        return productMapper.productConvertResponseDTO(product);
    }

    public List<ProductResponseDTO> getCategoryProducts(String prodCategory){

        List<Product> products = productRepository.findAll();

        if( !prodCategory.matches("^[A-Za-z ]+$")){
            throw new ProductCategoryException("Give a valid Product Category");
        }

        return products.stream()
                .filter(p -> p.getProdCategory().equalsIgnoreCase(prodCategory))
                .map(productMapper::productConvertResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getExpensiveProducts(){

        List<Product> products = productRepository.findAll();

        return products.stream()
                .sorted(Comparator.comparingDouble(Product::getProdPrice).reversed()).limit(3)
                .map(productMapper::productConvertResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByPrice(Double prodPrice){

        List<Product> products = productRepository.findProductsAbovePrice(prodPrice);

        return products.stream()
                .map(productMapper::productConvertResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO updateProduct(Integer productId,
                                            UpdateProductRequestDTO updateProductRequestDTO){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID: {} does not exists to Update", productId);
                    throw new ProductNotFoundException("Product with ID : " + productId + " does not exist to update");
                });

        if(updateProductRequestDTO.getProdName() != null)
            product.setProdName(updateProductRequestDTO.getProdName());
        if(updateProductRequestDTO.getProdCategory() != null)
            product.setProdCategory(updateProductRequestDTO.getProdCategory());
        if(updateProductRequestDTO.getProdPrice() != null)
            product.setProdPrice(updateProductRequestDTO.getProdPrice());

        product.setUpdatedAt(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        return productMapper.productConvertResponseDTO(updatedProduct);
    }

    public ProductResponseDTO deleteProduct(Integer productId){

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with ID: {} does not exists to Delete", productId);
                    throw new ProductNotFoundException("Product with ID : " + productId + " does not exist to delete");
                });

        productRepository.delete(product);

        return productMapper.productConvertResponseDTO(product);
    }
}
